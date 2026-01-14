package testing.proiectcolectivback.DTO;

import testing.proiectcolectivback.Domain.Movie;
import testing.proiectcolectivback.Domain.UserMovie;

public class UserMovieDto {
    private Long id;
    private String title;
    private String director;
    private String year;
    private String cover;
    private Double matchScore;
    private String reason;
    private String movieUrl;
    private boolean watched;

    public UserMovieDto() {}

    public static UserMovieDto fromUserMovie(UserMovie userMovie) {
        UserMovieDto dto = new UserMovieDto();
        Movie movie = userMovie.getMovie();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDirector(movie.getDirector());
        dto.setYear(movie.getYear());
        dto.setCover(movie.getCover());
        dto.setMatchScore(movie.getMatchScore());
        dto.setReason(movie.getReason());
        dto.setMovieUrl(movie.getMovieUrl());
        dto.setWatched(userMovie.isWatched());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }

    public Double getMatchScore() { return matchScore; }
    public void setMatchScore(Double matchScore) { this.matchScore = matchScore; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getMovieUrl() { return movieUrl; }
    public void setMovieUrl(String movieUrl) { this.movieUrl = movieUrl; }

    public boolean isWatched() { return watched; }
    public void setWatched(boolean watched) { this.watched = watched; }
}
