package fi.haagahelia.taskmaster.taskmaster.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface TicketRepository extends JpaRepository <Ticket, Long>{
    List<Ticket> findByTicketName(String ticketName);

    @Query("SELECT MAX(t.sortOrder) FROM Ticket t WHERE t.block.blockId = :blockId")
    Optional<Integer> findMaxSortOrderByBlockId(@Param("blockId") Long blockId);
}
