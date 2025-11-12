package testing.proiectcolectivback.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testing.proiectcolectivback.DTO.IncomingBooksDto;
import testing.proiectcolectivback.Domain.Book;
import testing.proiectcolectivback.Service.BookService;

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
}
