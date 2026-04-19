package org.example.library.service;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.CreateBookRequest;
import org.example.library.entity.BookEntity;
import org.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final BookRepository bookRepository;

    // Нарушение DIP: зависимости создаются вручную
    private final AuditService auditService = new AuditService();
    private final NotificationService notificationService = new NotificationService();
    private final BookExportService bookExportService = new BookExportService();

    public List<BookEntity> findAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<BookEntity> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public BookEntity createBook(CreateBookRequest request) {
        // Нарушение KISS/Separation: много всего в одном месте
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new IllegalArgumentException("author is required");
        }
        if (request.getShelfType() == null || request.getShelfType().isBlank()) {
            throw new IllegalArgumentException("shelfType is required");
        }

        BookEntity entity = new BookEntity();
        entity.setId(request.getId());
        entity.setTitle(request.getTitle().trim());
        entity.setAuthor(request.getAuthor().trim());
        entity.setCategory(request.getCategory() == null ? "UNKNOWN" : request.getCategory().trim());
        entity.setShelfType(request.getShelfType().trim().toUpperCase(Locale.ROOT));
        entity.setBorrowed(false);
        entity.setBorrowedBy(null);
        entity.setBorrowDays(null);
        entity.setFuturePremiumTag("NOT_USED_BUT_ALREADY_HERE");

        auditService.write("Creating book " + entity.getTitle());
        return bookRepository.save(entity);
    }

    public BookEntity borrowBook(Long id, String readerName, Integer days) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));

        // DRY: эти проверки частично повторяются ещё и в контроллере
        if (readerName == null || readerName.isBlank()) {
            throw new IllegalArgumentException("readerName is required");
        }
        if (days == null || days <= 0) {
            throw new IllegalArgumentException("days must be positive");
        }
        if (book.isBorrowed()) {
            throw new IllegalStateException("Book is already borrowed");
        }

        // Здесь явно просится Strategy, пока оставлен if/else
        if ("READING_ROOM".equalsIgnoreCase(book.getShelfType())) {
            throw new IllegalStateException("Reading room books cannot be taken home");
        } else if ("HOME_LOAN".equalsIgnoreCase(book.getShelfType())) {
            if (days > 30) {
                throw new IllegalArgumentException("Home loan books can be borrowed for max 30 days");
            }
            book.setBorrowed(true);
            book.setBorrowedBy(readerName.trim());
            book.setBorrowDays(days);
            notificationService.notifyReader(readerName, "You borrowed '" + book.getTitle() + "' for " + days + " days");
            auditService.write("Book borrowed: " + book.getId());
            return bookRepository.save(book);
        } else {
            throw new IllegalArgumentException("Unknown shelf type: " + book.getShelfType());
        }
    }

    public BookEntity returnBook(Long id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));

        if (!book.isBorrowed()) {
            throw new IllegalStateException("Book is not borrowed");
        }

        String readerName = book.getBorrowedBy();
        book.setBorrowed(false);
        book.setBorrowedBy(null);
        book.setBorrowDays(null);

        auditService.write("Book returned: " + book.getId());
        if (readerName != null && !readerName.isBlank()) {
            notificationService.notifyReader(readerName, "Thank you for returning '" + book.getTitle() + "'");
        }
        return bookRepository.save(book);
    }

    public String exportBooks(String format) {
        return bookExportService.export(bookRepository.findAll(), format);
    }

    public List<BookEntity> findOnlyAvailableBooksUsingVeryLongMethodNameBecauseMaybeOneDayWeWillNeedDifferentKindsOfAvailabilityLogic() {
        List<BookEntity> all = bookRepository.findAll();
        List<BookEntity> result = new ArrayList<>();
        for (BookEntity book : all) {
            if (!book.isBorrowed()) {
                result.add(book);
            }
        }
        return result;
    }

    // YAGNI: специально лишний метод "на будущее"
    public void reserveForFuturePremiumMembersWhoMightExistLater(Long id, String memberCode, String extraColorTag) {
        System.out.println("Not implemented, but maybe useful in 2040: " + id + memberCode + extraColorTag);
    }
}
