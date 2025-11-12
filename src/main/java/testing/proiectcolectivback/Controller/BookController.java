package testing.proiectcolectivback.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testing.proiectcolectivback.DTO.IncomingBooksDto;
import testing.proiectcolectivback.Domain.Book;
import testing.proiectcolectivback.Service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService svc) {
        this.bookService = svc;
    }

    @PostMapping
    public ResponseEntity<?> saveBookData(@RequestBody IncomingBooksDto dto) {
        try {
            Book saved = bookService.saveFromDto(dto);
            return ResponseEntity.ok().body(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAll();
        return ResponseEntity.ok().body(books);
    }
}
