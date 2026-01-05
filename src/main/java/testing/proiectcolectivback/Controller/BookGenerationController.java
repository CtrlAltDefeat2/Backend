package testing.proiectcolectivback.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import testing.proiectcolectivback.Domain.Book;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BookGenerationController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(
            value = "/generate-books",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Book> generateBooks(@RequestBody List<Map<String, Object>> musicDataList) {

        String aiUrl = "http://localhost:5000/predict";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Map<String, Object>>> requestEntity =
                new HttpEntity<>(musicDataList, headers);

        //  POST la AI
        ResponseEntity<Book[]> response = restTemplate.postForEntity(
                aiUrl,
                requestEntity,
                Book[].class
        );

        // De aici trebuie salvate cartile in repository , sa se asigure ca au image url si tot si abia apoi trimise inspre clientdeci return-ul va ramane ultimu
        // Se va face la fel pt moovies

        return List.of(response.getBody());
    }
}
