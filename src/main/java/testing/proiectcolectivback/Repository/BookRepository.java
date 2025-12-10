package testing.proiectcolectivback.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testing.proiectcolectivback.Domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByImageUrlIsNullOrImageUrlEquals(String imageUrl);
    Optional<Book> findByTitle(String title);
}
