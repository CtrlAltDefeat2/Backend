package testing.proiectcolectivback.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class AppUser {

    @Id
    private String id; // spotify user id

    @Column(name="email")
    private String email;

    @Column(name="displayName")
    private String displayName;

    public AppUser() {}
    public AppUser(String id, String email, String displayName) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

