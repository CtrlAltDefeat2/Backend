package testing.proiectcolectivback.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import testing.proiectcolectivback.Domain.Movie;
import testing.proiectcolectivback.Service.TmdbMovieCoverService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class MovieGenerationController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TmdbMovieCoverService tmdbMovieCoverService;

    @PostMapping(
            value = "/generate-movies",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Movie> generateMovies(@RequestBody Map<String, Object> requestBody) {

        String playlistId = (String) requestBody.get("playlistId");
        String playlistName = (String) requestBody.get("playlistName");
        Integer tracksTotal = (Integer) requestBody.get("tracksTotal");
        List<Map<String, Object>> songFeatures = (List<Map<String, Object>>) requestBody.get("songFeatures");

        String aiUrl = "http://127.0.0.1:8000/predict-movies";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> aiRequest = Map.of(
                "playlistId", playlistId,
                "playlistName", playlistName,
                "tracksTotal", tracksTotal,
                "features", songFeatures
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(aiRequest, headers);

        Map<String, Object> aiResponse = restTemplate.exchange(
                aiUrl,
                HttpMethod.POST,
                entity,
                Map.class
        ).getBody();

        List<Map<String, Object>> recommendedMoviesData = (List<Map<String, Object>>) aiResponse.get("movies");

        List<Movie> recommendedMovies = new ArrayList<>();

        for (Map<String, Object> data : recommendedMoviesData) {
            String title = data.get("Titlu").toString();
            String author = data.get("Director").toString();
            double match = Double.parseDouble(data.get("similarity").toString());

            Movie movie = tmdbMovieCoverService.findOrCreateWithCover(title, author, match);
            movie.setMatchScore(match);
            recommendedMovies.add(movie);

            System.out.println(movie.getTitle());
        }


        return recommendedMovies;
    }
}
