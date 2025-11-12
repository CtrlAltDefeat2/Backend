package testing.proiectcolectivback.DTO;

import java.util.Map;

public class IncomingBooksDto {
    private String book_title;
    private String authors;
    private Map<String, Integer> emotions;

    public String getBook_title() {
        return book_title;
    }
    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }
    public String getAuthors() {
        return authors;
    }
    public void setAuthors(String authors) {
        this.authors = authors;
    }
    public Map<String, Integer> getEmotions() {
        return emotions;
    }
    public void setEmotions(Map<String, Integer> emotions) {
        this.emotions = emotions;
    }
}
