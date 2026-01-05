package testing.proiectcolectivback.Domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="user_movies", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "movie_id"}))
public class UserMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private LocalDateTime savedAt = LocalDateTime.now();

    public UserMovie() {}

    public UserMovie(AppUser user, Movie movie) {
        this.user = user;
        this.movie = movie;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public AppUser getUser() {
        return user;
    }
    public void setUser(AppUser user) {
        this.user = user;
    }
    public Movie getMovie() {
        return movie;
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
