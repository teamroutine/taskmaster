package fi.haagahelia.taskmaster.taskmaster.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
      List<AppUser> findByFirstName(String firstName);

      Optional<AppUser> findByUsername(String username);

      Optional<AppUser> findOneById(Long id);

}
