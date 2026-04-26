package org.example.library.config;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.CreateBookRequest;
import org.example.library.repository.BookRepository;
import org.example.library.service.LibraryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final LibraryService libraryService;
    private final BookRepository bookRepository;

    @Override
    public void run(String... args) {
        if (bookRepository.count() > 0) {
            return;
        }

        libraryService.createBook(request(1L, "Clean Code", "Robert C. Martin", "Programming", "HOME_LOAN"));
        libraryService.createBook(request(2L, "Design Patterns", "GoF", "Programming", "HOME_LOAN"));
        libraryService.createBook(request(3L, "Ancient Maps Atlas", "City Archive", "History", "READING_ROOM"));
        libraryService.createBook(request(4L, "Rare Medieval Manuscripts", "Museum Press", "History", "READING_ROOM"));
    }

    private CreateBookRequest request(Long id, String title, String author, String category, String shelfType) {
        CreateBookRequest request = new CreateBookRequest();
        request.setId(id);
        request.setTitle(title);
        request.setAuthor(author);
        request.setCategory(category);
        request.setShelfType(shelfType);
        return request;
    }
}
