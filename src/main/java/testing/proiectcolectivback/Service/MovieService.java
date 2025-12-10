package testing.proiectcolectivback.Service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import testing.proiectcolectivback.Domain.AppUser;
import testing.proiectcolectivback.Domain.Movie;
import testing.proiectcolectivback.Domain.UserMovie;
import testing.proiectcolectivback.Repository.MovieRepository;
import testing.proiectcolectivback.Repository.UserMovieRepository;
import testing.proiectcolectivback.Repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final UserMovieRepository userMovieRepository;
    private final UserRepository userRepository;

    public MovieService(MovieRepository movieRepository, UserMovieRepository userMovieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userMovieRepository = userMovieRepository;
        this.userRepository = userRepository;
    }

    private AppUser getCurrentUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found" + userId));
    }

    public List<Movie> getMyMovies() {
        AppUser user = getCurrentUser();

        return userMovieRepository.findByUser(user)
                .stream()
                .map(UserMovie::getMovie)
                .collect(Collectors.toList());
    }

    public Movie addMovie(Movie movie) {
        AppUser user = getCurrentUser();

        Movie saved = movieRepository.save(movie);

        if(userMovieRepository.existsByUserAndMovie(user, saved)) {
            return saved;
        }

        UserMovie userMovie = new UserMovie(user, saved);
        userMovieRepository.save(userMovie);
        return saved;
    }

    public void removeMovie(Long movieId) {
        AppUser user = getCurrentUser();

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie Not Found"));

        UserMovie link = userMovieRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new RuntimeException("This movie is not in the list"));

        userMovieRepository.delete(link);
    }
}
