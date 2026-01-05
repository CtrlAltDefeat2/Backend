package testing.proiectcolectivback.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="titlu", nullable = false)
    private String title;

    @Column(name="director", nullable = false)
    private String director;

    @Column(name="year")
    private String year;

    @Column(name="cover")
    private String cover;

    @Column(name="match_score")
    private Double matchScore;

    @Column(name="reason")
    private String reason;

    @Column(name="movie_url")
    private String movieUrl;

    public Movie() {}

    public Movie(String title, String director, String year, String cover, Double matchScore, String reason, String movieUrl) {
        this.title = title;
        this.director = director;
        this.year = year;
        this.cover = cover;
        this.matchScore = matchScore;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getCover() {
        return cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public Double getMatchScore() {
        return matchScore;
    }
    public void setMatchScore(Double matchScore) {
        this.matchScore = matchScore;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getMovieUrl() {
        return movieUrl;
    }
    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }
}
