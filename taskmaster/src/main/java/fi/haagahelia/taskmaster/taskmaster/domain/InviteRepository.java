package fi.haagahelia.taskmaster.taskmaster.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    Optional<Invite> findByNanoId(String nanoId);
}