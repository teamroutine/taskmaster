package fi.haagahelia.taskmaster.taskmaster.domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long>{
    List<Block> findByBlockName(String blockName);
    List<Block> findByBlockNameAndPanel_PanelId(String blockName, Long panelId);
}
