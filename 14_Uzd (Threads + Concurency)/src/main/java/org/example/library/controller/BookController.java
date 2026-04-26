package org.example.library.controller;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.BookActionResponse;
import org.example.library.dto.BookQueueResponse;
import org.example.library.dto.BookDto;
import org.example.library.dto.BorrowBookRequest;
import org.example.library.dto.CreateBookRequest;
import org.example.library.dto.ExtendBorrowRequest;
import org.example.library.dto.QueueBookRequest;
import org.example.library.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookController {

    private final LibraryService libraryService;

    @GetMapping
    public List<BookDto> getAllBooks() {
        return libraryService.findAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return libraryService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody CreateBookRequest request) {
        return ResponseEntity.ok(libraryService.createBook(request));
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<BookActionResponse> borrowBook(@PathVariable Long id,
                                                         @RequestBody BorrowBookRequest request) {
        return ResponseEntity.ok(libraryService.borrowBook(id, request));
    }

    @PostMapping("/{id}/extend")
    public ResponseEntity<BookActionResponse> extendBorrow(@PathVariable Long id,
                                                           @RequestBody ExtendBorrowRequest request) {
        return ResponseEntity.ok(libraryService.extendBorrow(id, request));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<BookActionResponse> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(libraryService.returnBook(id));
    }

    @PostMapping("/{id}/queue")
    public ResponseEntity<BookQueueResponse> joinQueue(@PathVariable Long id,
                                                       @RequestBody QueueBookRequest request) {
        return ResponseEntity.ok(libraryService.joinQueue(id, request));
    }

    @GetMapping("/{id}/queue")
    public ResponseEntity<BookQueueResponse> getQueue(@PathVariable Long id) {
        return ResponseEntity.ok(libraryService.getQueue(id));
    }

    @GetMapping("/export")
    public String exportBooks(@RequestParam(defaultValue = "txt") String format) {
        return libraryService.exportBooks(format);
    }
}
