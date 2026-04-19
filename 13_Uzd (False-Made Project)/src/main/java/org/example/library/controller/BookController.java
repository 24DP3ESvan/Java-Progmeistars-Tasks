package org.example.library.controller;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.CreateBookRequest;
import org.example.library.entity.BookEntity;
import org.example.library.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookController {

    private final LibraryService libraryService;

    @GetMapping
    public List<BookEntity> getAllBooks() {
        return libraryService.findAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable Long id) {
        return libraryService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookEntity> createBook(@RequestBody CreateBookRequest request) {
        // Дублирование простой валидации с сервисом
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(libraryService.createBook(request));
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<Map<String, Object>> borrowBook(@PathVariable Long id,
                                                          @RequestParam String readerName,
                                                          @RequestParam Integer days) {
        // Контроллер лезет в бизнес-правила
        if (readerName == null || readerName.isBlank()) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("message", "readerName is required");
            error.put("status", 400);
            return ResponseEntity.badRequest().body(error);
        }
        if (days == null || days <= 0) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("message", "days must be positive");
            error.put("status", 400);
            return ResponseEntity.badRequest().body(error);
        }

        BookEntity book = libraryService.borrowBook(id, readerName, days);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Book borrowed successfully");
        response.put("bookId", book.getId());
        response.put("title", book.getTitle());
        response.put("reader", book.getBorrowedBy());
        response.put("days", book.getBorrowDays());
        response.put("rawBook", book); // entity торчит наружу
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/return")
    public BookEntity returnBook(@PathVariable Long id) {
        return libraryService.returnBook(id);
    }

    @GetMapping("/export")
    public String exportBooks(@RequestParam(defaultValue = "txt") String format) {
        return libraryService.exportBooks(format);
    }
}
