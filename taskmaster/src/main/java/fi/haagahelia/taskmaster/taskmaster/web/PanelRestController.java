package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.taskmaster.taskmaster.domain.Panel;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/panels")
public class PanelRestController {

    private final PanelRepository panelRepository;

    @Autowired
    public PanelRestController(PanelRepository panelRepository) {
        this.panelRepository = panelRepository;
    }

    // Get all the teams Panels
    @GetMapping
    public ResponseEntity<List<Panel>> getAllPanels() {
        List<Panel> panels = panelRepository.findAll();
        return ResponseEntity.ok(panels);
    }

}
