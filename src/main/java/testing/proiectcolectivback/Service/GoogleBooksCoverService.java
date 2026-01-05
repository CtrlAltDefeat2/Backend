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
import java.util.List;

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

    public Book findOrCreateWithCover(String title, String authors, double match) {
        List<Book> existingBooks = bookRepository.findByTitleAndAuthors(title, authors);
        if (!existingBooks.isEmpty()) {
            return existingBooks.getFirst();
        }

        Book newBook = new Book(title, authors, match);

        if (title == null || title.isBlank()) {
            newBook.setImageUrl("https://www.freeiconspng.com/thumbs/question-mark-icon/black-question-mark-icon-clip-art-10.png");
        } else {
            String q = buildQuery(newBook);
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://www.googleapis.com/books/v1/volumes")
                    .queryParam("q", q)
                    .queryParam("maxResults", 1)
                    .queryParam("key", apiKey)
                    .build()
                    .encode()
                    .toUri();

            try {
                GoogleBooksResponse response = restTemplate.getForObject(uri, GoogleBooksResponse.class);
                if (response != null && response.getItems() != null && !response.getItems().isEmpty()) {
                    GoogleBookItem item = response.getItems().get(0);
                    VolumeInfo info = item.getVolumeInfo();
                    if (info != null && info.getImageLinks() != null) {
                        ImageLinks links = info.getImageLinks();
                        String cover = links.getThumbnail() != null ? links.getThumbnail() : links.getSmallThumbnail();
                        if (cover != null) {
                            if (cover.startsWith("http://")) {
                                cover = cover.replace("http://", "https://");
                            }
                            newBook.setImageUrl(cover);
                        } else {
                            newBook.setImageUrl("https://www.freeiconspng.com/thumbs/question-mark-icon/black-question-mark-icon-clip-art-10.png");
                        }
                    } else {
                        newBook.setImageUrl("https://www.freeiconspng.com/thumbs/question-mark-icon/black-question-mark-icon-clip-art-10.png");
                    }
                } else {
                    newBook.setImageUrl("https://www.freeiconspng.com/thumbs/question-mark-icon/black-question-mark-icon-clip-art-10.png");
                }
            } catch (Exception e) {
                log.warn("Error calling Google Books for '{}': {}", newBook.getTitle(), e.getMessage());
                newBook.setImageUrl("https://www.freeiconspng.com/thumbs/question-mark-icon/black-question-mark-icon-clip-art-10.png");
            }
        }

        bookRepository.save(newBook);
        log.info("Saved book with cover: title='{}', imageUrl={}", newBook.getTitle(), newBook.getImageUrl());
        return newBook;
    }

    private String buildQuery(Book book) {
        String titlePart = "intitle:" + book.getTitle();
        String authorPart = (book.getAuthors() != null && !book.getAuthors().isBlank()) ? " inauthor:" + book.getAuthors() : "";
        return titlePart + authorPart;
    }
}

