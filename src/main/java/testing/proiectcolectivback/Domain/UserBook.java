package testing.proiectcolectivback.Domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="user_books", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "book_id"}))
public class UserBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDateTime savedAt = LocalDateTime.now();

    private boolean read = false;

    public UserBook() {}

    public UserBook(AppUser user, Book book) {
        this.user = user;
        this.book = book;
    }
    public Book getBook() {
        return book;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
