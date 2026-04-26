package org.example.library.service;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.BookActionResponse;
import org.example.library.dto.BookQueueResponse;
import org.example.library.dto.BookDto;
import org.example.library.dto.BorrowBookRequest;
import org.example.library.dto.CreateBookRequest;
import org.example.library.dto.ExtendBorrowRequest;
import org.example.library.dto.QueueBookRequest;
import org.example.library.entity.BookEntity;
import org.example.library.mapper.BookMapper;
import org.example.library.repository.BookRepository;
import org.example.library.service.export.BookExporterFactory;
import org.example.library.service.policy.BorrowPolicy;
import org.example.library.service.policy.BorrowPolicyFactory;
import org.example.library.service.validation.BookRequestValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookRequestValidator validator;
    private final BorrowPolicyFactory borrowPolicyFactory;
    private final BookExporterFactory bookExporterFactory;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final WaitlistService waitlistService;

    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public Optional<BookDto> findBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto);
    }

    public BookDto createBook(CreateBookRequest request) {
        validator.validateCreateRequest(request);

        BookEntity entity = bookMapper.toEntity(request);
        auditService.write("Creating book " + entity.getTitle());

        return bookMapper.toDto(bookRepository.save(entity));
    }

    public BookActionResponse borrowBook(Long id, BorrowBookRequest request) {
        validator.validateBorrowRequest(request);

        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));

        // Somewhere around this check-and-change flow there is still a concurrency problem.
        if (book.isBorrowed()) {
            throw new IllegalStateException("Book is already borrowed");
        }

        BorrowPolicy policy = borrowPolicyFactory.getPolicy(book.getShelfType());
        policy.validate(book, request.getDays());

        book.setBorrowed(true);
        book.setBorrowedBy(request.getReaderName().trim());
        book.setBorrowDays(request.getDays());

        BookEntity savedBook = bookRepository.save(book);
        waitlistService.removeReader(id, request.getReaderName());

        // This side effect may also become interesting when requests happen at the same time.
        notificationService.notifyReader(
                request.getReaderName(),
                "You borrowed '" + savedBook.getTitle() + "' for " + request.getDays() + " days"
        );
        auditService.write("Book borrowed: " + savedBook.getId());

        return BookActionResponse.builder()
                .message("Book borrowed successfully")
                .book(bookMapper.toDto(savedBook))
                .build();
    }

    public BookActionResponse extendBorrow(Long id, ExtendBorrowRequest request) {
        validator.validateExtendRequest(request);

        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));

        if (!book.isBorrowed()) {
            throw new IllegalStateException("Book is not borrowed");
        }

        // This read-modify-write flow can also get interesting with concurrent requests.
        int currentDays = book.getBorrowDays() == null ? 0 : book.getBorrowDays();
        int extendedDays = currentDays + request.getAdditionalDays();

        BorrowPolicy policy = borrowPolicyFactory.getPolicy(book.getShelfType());
        policy.validate(book, extendedDays);

        book.setBorrowDays(extendedDays);
        BookEntity savedBook = bookRepository.save(book);

        if (savedBook.getBorrowedBy() != null && !savedBook.getBorrowedBy().isBlank()) {
            notificationService.notifyReader(
                    savedBook.getBorrowedBy(),
                    "Loan for '" + savedBook.getTitle() + "' extended to " + extendedDays + " days"
            );
        }
        auditService.write("Book extended: " + savedBook.getId());

        return BookActionResponse.builder()
                .message("Book loan extended successfully")
                .book(bookMapper.toDto(savedBook))
                .build();
    }

    public BookActionResponse returnBook(Long id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));

        // Somewhere here two operations can disagree about the current state of the book.
        if (!book.isBorrowed()) {
            throw new IllegalStateException("Book is not borrowed");
        }

        String readerName = book.getBorrowedBy();
        book.setBorrowed(false);
        book.setBorrowedBy(null);
        book.setBorrowDays(null);

        BookEntity savedBook = bookRepository.save(book);
        auditService.write("Book returned: " + savedBook.getId());
        String nextReader = waitlistService.peekNextReader(id);

        if (readerName != null && !readerName.isBlank()) {
            notificationService.notifyReader(readerName, "Thank you for returning '" + savedBook.getTitle() + "'");
        }
        if (nextReader != null && !nextReader.isBlank()) {
            notificationService.notifyReader(nextReader, "'" + savedBook.getTitle() + "' is now available for you");
            waitlistService.removeNextReader(id);
        }

        return BookActionResponse.builder()
                .message("Book returned successfully")
                .book(bookMapper.toDto(savedBook))
                .build();
    }

    public BookQueueResponse joinQueue(Long id, QueueBookRequest request) {
        validator.validateQueueRequest(request);

        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));

        if (!book.isBorrowed()) {
            throw new IllegalStateException("Book is currently available");
        }

        // The waiting list is intentionally simple and becomes more interesting under concurrent access.
        int queueSize = waitlistService.addReader(id, request.getReaderName());
        auditService.write("Reader joined queue for book " + id + ": " + request.getReaderName().trim());

        return BookQueueResponse.builder()
                .bookId(id)
                .size(queueSize)
                .readers(waitlistService.getReaders(id))
                .build();
    }

    public BookQueueResponse getQueue(Long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));

        List<String> readers = waitlistService.getReaders(id);
        return BookQueueResponse.builder()
                .bookId(id)
                .size(readers.size())
                .readers(readers)
                .build();
    }

    public String exportBooks(String format) {
        List<BookDto> books = findAllBooks();
        return bookExporterFactory.getExporter(format).export(books);
    }
}
