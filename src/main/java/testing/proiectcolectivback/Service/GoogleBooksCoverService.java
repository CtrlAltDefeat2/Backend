package testing.proiectcolectivback.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import testing.proiectcolectivback.Domain.Book;
import testing.proiectcolectivback.DTO.GoogleBooksResponse;
import testing.proiectcolectivback.DTO.GoogleBookItem;
import testing.proiectcolectivback.DTO.VolumeInfo;
import testing.proiectcolectivback.DTO.ImageLinks;
import testing.proiectcolectivback.Repository.BookRepository;

import java.net.URI;

@Service
public class GoogleBooksCoverService {

    private static final Logger log = LoggerFactory.getLogger(GoogleBooksCoverService.class);

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    private final String apiKey;

    public GoogleBooksCoverService(BookRepository bookRepository,
                                   RestTemplate restTemplate,
                                   @Value("${googlebooks.apiKey}") String apiKey) {
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    /**
     * Asta o chemi imediat după ce ai salvat cartea.
     */
    public void updateCoverForBook(Book book) {
        // dacă deja are cover (eventual îl setezi manual), nu mai facem nimic
        if (book.getImageUrl() != null && !book.getImageUrl().isBlank()) {
            return;
        }
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            return;
        }

        String q = buildQuery(book);

        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://www.googleapis.com/books/v1/volumes")
                .queryParam("q", q)
                .queryParam("maxResults", 1)
                .queryParam("key", apiKey)
                .build()
                .encode()
                .toUri();


        GoogleBooksResponse response;
        try {
            response = restTemplate.getForObject(uri, GoogleBooksResponse.class);
        } catch (Exception e) {
            log.warn("Error calling Google Books for '{}': {}", book.getTitle(), e.getMessage());
            return;
        }

        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
            log.info("No Google Books results for '{}'", book.getTitle());
            return;
        }

        GoogleBookItem item = response.getItems().get(0);
        VolumeInfo info = item.getVolumeInfo();
        if (info == null || info.getImageLinks() == null) {
            log.info("No image links for '{}'", book.getTitle());
            return;
        }

        ImageLinks links = info.getImageLinks();
        String cover = links.getThumbnail() != null ? links.getThumbnail() : links.getSmallThumbnail();
        if (cover == null) {
            log.info("No thumbnail for '{}'", book.getTitle());
            return;
        }

        if (cover.startsWith("http://")) {
            cover = cover.replace("http://", "https://");
        }

        book.setImageUrl(cover);
        bookRepository.save(book);

        log.info("Saved cover for book id={} title='{}' imageUrl={}",
                book.getId(), book.getTitle(), book.getImageUrl());
    }

    private String buildQuery(Book book) {
        String titlePart = "intitle:" + book.getTitle();
        String authorPart = "";

        if (book.getAuthors() != null && !book.getAuthors().isBlank()) {
            // spațiu normal, UriComponentsBuilder îl encodează în + sau %20
            authorPart = " inauthor:" + book.getAuthors();
        }

        // Ex: "intitle:Atomic Habits inauthor:James Clear"
        return titlePart + authorPart;
    }




    private String quote(String s) {
        return s;
    }
}
