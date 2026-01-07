package testing.proiectcolectivback.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import testing.proiectcolectivback.Domain.Movie;
import testing.proiectcolectivback.DTO.TmdbSearchResponse;
import testing.proiectcolectivback.DTO.TmdbMovieResult;
import testing.proiectcolectivback.Repository.MovieRepository;

import java.net.URI;
import java.util.List;

@Service
public class TmdbMovieCoverService {

    private static final Logger log = LoggerFactory.getLogger(TmdbMovieCoverService.class);

    private static final String TMDB_IMAGE_BASE = "https://image.tmdb.org/t/p/w500";
    private static final String FALLBACK_IMAGE =
            "https://www.freeiconspng.com/thumbs/question-mark-icon/black-question-mark-icon-clip-art-10.png";

    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;
    private final String apiKey;

    public TmdbMovieCoverService(MovieRepository movieRepository,
                                 RestTemplate restTemplate,
                                 @Value("${tmdb.apiKey}") String apiKey) {
        this.movieRepository = movieRepository;
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public Movie findOrCreateWithCover(String title, String director, double match) {
        List<Movie> existingMovies = movieRepository.findMovieByTitleAndDirector(title, director);
        if (!existingMovies.isEmpty()) {
            return existingMovies.getFirst();
        }

        Movie newMovie = new Movie(title, director, match);

        if (title == null || title.isBlank()) {
            newMovie.setCover(FALLBACK_IMAGE);
        } else {
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://api.themoviedb.org/3/search/movie")
                    .queryParam("api_key", apiKey)
                    .queryParam("query", title)
                    .queryParam("include_adult", false)
                    .build()
                    .encode()
                    .toUri();

            try {
                TmdbSearchResponse response =
                        restTemplate.getForObject(uri, TmdbSearchResponse.class);

                if (response != null &&
                        response.getResults() != null &&
                        !response.getResults().isEmpty()) {

                    TmdbMovieResult result = response.getResults().get(0);
                    if (result.getPosterPath() != null) {
                        newMovie.setCover(TMDB_IMAGE_BASE + result.getPosterPath());
                    } else {
                        newMovie.setCover(FALLBACK_IMAGE);
                    }

                } else {
                    newMovie.setCover(FALLBACK_IMAGE);
                }
            } catch (Exception e) {
                log.warn("Error calling TMDB for '{}': {}", title, e.getMessage());
                newMovie.setCover(FALLBACK_IMAGE);
            }
        }

        movieRepository.save(newMovie);
        log.info("Saved movie with cover: title='{}', imageUrl={}",
                newMovie.getTitle(), newMovie.getCover());

        return newMovie;
    }
}
