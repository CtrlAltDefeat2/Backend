package testing.proiectcolectivback.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testing.proiectcolectivback.Domain.AppUser;

public interface UserRepository extends JpaRepository<AppUser, String> {
}
