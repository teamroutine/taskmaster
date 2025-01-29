package fi.haagahelia.taskmaster.taskmaster.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PanelRepository extends JpaRepository<Panel, Long> {
    List<Panel> findByPanelName(String panelName);
}
