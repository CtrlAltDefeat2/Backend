package testing.proiectcolectivback.DTO;

import testing.proiectcolectivback.Domain.Book;
import testing.proiectcolectivback.Domain.UserBook;

import java.util.Map;

public class UserBookDto {
    private Long id;
    private String title;
    private String authors;
    private Map<String, Integer> emotions;
    private String imageUrl;
    private Double match;
    private boolean read;

    public UserBookDto() {}

    public static UserBookDto fromUserBook(UserBook userBook) {
        UserBookDto dto = new UserBookDto();
        Book book = userBook.getBook();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthors(book.getAuthors());
        dto.setEmotions(book.getEmotions());
        dto.setImageUrl(book.getImageUrl());
        dto.setMatch(book.getMatch());
        dto.setRead(userBook.isRead());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }

    public Map<String, Integer> getEmotions() { return emotions; }
    public void setEmotions(Map<String, Integer> emotions) { this.emotions = emotions; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Double getMatch() { return match; }
    public void setMatch(Double match) { this.match = match; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}
