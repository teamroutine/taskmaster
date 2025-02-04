package fi.haagahelia.taskmaster.taskmaster.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository <Ticket, Long>{
    List<Ticket> findByTicketName(String ticketName);

}
