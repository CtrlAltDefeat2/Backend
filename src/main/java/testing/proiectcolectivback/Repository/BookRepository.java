package testing.proiectcolectivback.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testing.proiectcolectivback.Domain.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
