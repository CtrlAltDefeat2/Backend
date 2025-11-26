package testing.proiectcolectivback.Service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import testing.proiectcolectivback.DTO.IncomingBooksDto;
import testing.proiectcolectivback.Domain.AppUser;
import testing.proiectcolectivback.Domain.Book;
import testing.proiectcolectivback.Domain.UserBook;
import testing.proiectcolectivback.Repository.BookRepository;
import testing.proiectcolectivback.Repository.UserBookRepository;
import testing.proiectcolectivback.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;
    private final UserRepository userRepository;
    private final GoogleBooksCoverService coverService;

    public BookService(BookRepository bookRepository, UserBookRepository userBookRepository, UserRepository userRepository, GoogleBooksCoverService coverService) {
        this.bookRepository = bookRepository;
        this.userBookRepository = userBookRepository;
        this.userRepository = userRepository;
        this.coverService = coverService;
    }

    private AppUser getCurrentUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found" + userId));
    }

    @Transactional
    public Book saveFromDto(IncomingBooksDto dto) {
        if(dto == null || dto.getBook_title() == null) {
            throw new IllegalArgumentException("Book title cannot be null");
        }
        Optional<Book> existing = bookRepository.findByTitle(dto.getBook_title());
        Book book = existing.orElseGet(() ->{
            Book b = new Book(dto.getBook_title(), dto.getAuthors(), dto.getEmotions());
            return bookRepository.save(b);
        });
        AppUser user = getCurrentUser();
        boolean exists  =userBookRepository.existsByUserAndBook(user, book);

        if(!exists) {
            userBookRepository.save(new UserBook(user, book));
        }
        return book;
    }

    public List<Book> getBooksForCurrentUser() {
        AppUser user = getCurrentUser();
        return bookRepository.findBooksByUserId(user.getId());
    }

    public void removeBook(Long bookId) {
        AppUser user = getCurrentUser();
        userRepository.deleteByUserIdAndBookId(user.getId(), bookId);
    }

}
