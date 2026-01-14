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
        Book book = bookRepository.findByTitle(dto.getTitle())
                .orElseGet(() -> {
                    Book b = new Book();
                    b.setTitle(dto.getTitle());
                    b.setAuthors(dto.getAuthors());
                    b.setImageUrl(dto.getCover());
                    return bookRepository.save(b);
                });

        AppUser user = getCurrentUser();

        if (!userBookRepository.existsByUserAndBook(user, book)) {
            UserBook userBook = new UserBook(user, book);
            userBookRepository.save(userBook);
        }
        return book;
    }

    public List<Book> getBooksForCurrentUser() {
        AppUser user = getCurrentUser();
        return userBookRepository.findByUser(user)
                .stream()
                .map(UserBook::getBook)
                .toList();
    }

    @Transactional
    public void removeBook(Long bookId) {
        AppUser user = getCurrentUser();
        userBookRepository.deleteByUserIdAndBookId(user.getId(), bookId);
    }

    @Transactional
    public boolean toggleRead(Long bookId) {
        AppUser user = getCurrentUser();
        UserBook userBook = userBookRepository.findByUserIdAndBookId(user.getId(), bookId)
                .orElseThrow(() -> new RuntimeException("UserBook not found for userId: " + user.getId() + " and bookId: " + bookId));
        userBook.setRead(!userBook.isRead());
        userBookRepository.save(userBook);
        return userBook.isRead();
    }

    @Transactional
    public void removeAllBooks() {
        AppUser user = getCurrentUser();
        userBookRepository.deleteByUser(user);
    }
}
