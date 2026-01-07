package testing.proiectcolectivback.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testing.proiectcolectivback.Domain.AppUser;
import testing.proiectcolectivback.Domain.Movie;
import testing.proiectcolectivback.Domain.UserMovie;

import java.util.List;
import java.util.Optional;

public interface UserMovieRepository extends JpaRepository<UserMovie, Long> {
    List<UserMovie> findByUser(AppUser user);

    boolean existsByUserAndMovie(AppUser user, Movie movie);

    Optional<UserMovie> findByUserAndMovie(AppUser user, Movie movie);
}
