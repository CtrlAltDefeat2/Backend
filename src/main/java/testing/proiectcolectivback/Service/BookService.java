package testing.proiectcolectivback.Service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import testing.proiectcolectivback.DTO.IncomingBooksDto;
import testing.proiectcolectivback.Domain.Book;
import testing.proiectcolectivback.Repository.BookRepository;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Book saveFromDto(IncomingBooksDto dto) {
        if(dto == null || dto.getBook_title() == null) {
            throw new IllegalArgumentException("Book title cannot be null");
        }
        Book book = new Book(dto.getBook_title(), dto.getAuthors(), dto.getEmotions());
        Book saved =  bookRepository.save(book);
        System.out.println(dto.getBook_title() + " " + dto.getAuthors() + " " + dto.getEmotions());
        System.out.println(saved.getId() + " " + saved.getAuthors());
        return saved;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
