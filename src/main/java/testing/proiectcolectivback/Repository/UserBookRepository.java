package testing.proiectcolectivback.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testing.proiectcolectivback.Domain.AppUser;
import testing.proiectcolectivback.Domain.Book;
import testing.proiectcolectivback.Domain.UserBook;

import java.util.List;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    List<UserBook> findByUser(AppUser user);
    boolean existsByUserAndBook(AppUser user, Book book);
    void deleteByUserIdAndBookId(String userId, Long bookId);
}
