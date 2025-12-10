package testing.proiectcolectivback.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testing.proiectcolectivback.Domain.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
