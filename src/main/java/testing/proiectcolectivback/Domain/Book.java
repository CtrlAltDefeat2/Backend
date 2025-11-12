package testing.proiectcolectivback.Domain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="titlu", nullable = false)
    private String title;

    @Column(name="autor")
    private String authors;

    @Column(name="emotions", columnDefinition = "TEXT")
    private String emotionsJson;

    @Transient
    private Map<String, Integer> emotions;

    public Book() {}

    public Book(String title, String author, Map<String, Integer> emotions) {
        this.title = title;
        this.authors = author;
        setEmotions(emotions);
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public void setEmotions(Map<String, Integer> emotions) {
        this.emotions = emotions;
        try {
            this.emotionsJson = mapper.writeValueAsString(emotions);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing emotions",e);
        }
    }

    public Map<String, Integer> getEmotions() {
        if(emotions == null && emotionsJson != null) {
            try {
                emotions = mapper.readValue(emotionsJson, Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error deserializing emotions",e);
            }
        }
        return emotions;
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
    public String getAuthors() {
        return authors;
    }
    public void setAuthors(String authors) {
        this.authors = authors;
    }
    public String getEmotionsJson() {
        return emotionsJson;
    }
    public void setEmotionsJson(String emotions) {
        this.emotionsJson = emotions;
    }
}

