package testing.proiectcolectivback.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testing.proiectcolectivback.Domain.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByImageUrlIsNullOrImageUrlEquals(String imageUrl);
}
