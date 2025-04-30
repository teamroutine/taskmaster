package fi.haagahelia.taskmaster.taskmaster.domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface BlockRepository extends JpaRepository<Block, Long>{
    List<Block> findByBlockName(String blockName);
    List<Block> findByBlockNameAndPanel_PanelId(String blockName, Long panelId);

    @Query("SELECT MAX(b.sortOrder) FROM Block b WHERE b.panel.panelId = :panelId")
    Optional<Integer> findMaxSortOrderByPanelId(@Param("panelId") Long panelId);
}
