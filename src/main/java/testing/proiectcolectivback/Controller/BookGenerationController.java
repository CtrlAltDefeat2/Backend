package testing.proiectcolectivback.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import testing.proiectcolectivback.Domain.Book;
import testing.proiectcolectivback.Service.GoogleBooksCoverService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BookGenerationController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GoogleBooksCoverService googleBooksCoverService;

    @PostMapping(
            value = "/generate-books",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Book> generateBooks(@RequestBody Map<String, Object> requestBody) {

        String playlistId = (String) requestBody.get("playlistId");
        String playlistName = (String) requestBody.get("playlistName");
        Integer tracksTotal = (Integer) requestBody.get("tracksTotal");
        List<Map<String, Object>> songFeatures = (List<Map<String, Object>>) requestBody.get("songFeatures");

        String aiUrl = "http://127.0.0.1:8000/predict";
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

        List<Map<String, Object>> recommendedBooksData = (List<Map<String, Object>>) aiResponse.get("books");

        List<Book> recommendedBooks = new ArrayList<>();

        for (Map<String, Object> data : recommendedBooksData) {
            String title = data.get("Titlu").toString();
            String author = data.get("autor").toString();
            double match = Double.parseDouble(data.get("similarity_pct").toString());

            Book book = googleBooksCoverService.findOrCreateWithCover(title, author, match);
            book.setMatch(match);
            recommendedBooks.add(book);
        }



        return recommendedBooks;
    }
}
