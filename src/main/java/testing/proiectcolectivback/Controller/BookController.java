package testing.proiectcolectivback.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testing.proiectcolectivback.DTO.IncomingBooksDto;
import testing.proiectcolectivback.DTO.UserBookDto;
import testing.proiectcolectivback.Domain.Book;
import testing.proiectcolectivback.Service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {
    private final BookService bookService;

    public BookController(BookService svc) {
        this.bookService = svc;
    }

//    @PostMapping
//    public ResponseEntity<?> saveBookData(@RequestBody IncomingBooksDto dto) {
//        try {
//            Book saved = bookService.saveFromDto(dto);
//            return ResponseEntity.ok().body(saved);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @GetMapping("/me")
    public ResponseEntity<List<UserBookDto>> getMyBooks() {
        return ResponseEntity.ok(bookService.getBooksForCurrentUser());
    }

    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody IncomingBooksDto dto) {
        Book saved = bookService.saveFromDto(dto);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> removeBook(@PathVariable Long bookId) {
        bookService.removeBook(bookId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/toggle-read")
    public ResponseEntity<?> toggleRead(@RequestParam Long bookId) {
        try {
            boolean newStatus = bookService.toggleRead(bookId);
            return ResponseEntity.ok().body(newStatus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> clearReadingList() {
        bookService.removeAllBooks();
        return ResponseEntity.noContent().build();
    }
}
