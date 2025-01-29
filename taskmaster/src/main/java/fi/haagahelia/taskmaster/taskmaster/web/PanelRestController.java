package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.taskmaster.taskmaster.domain.Panel;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin
@RestController
@RequestMapping("/api/panels")
public class PanelRestController {

    private final PanelRepository panelRepository;

    @Autowired
    public PanelRestController(PanelRepository panelRepository) {
        this.panelRepository = panelRepository;
    }

    // Get all the team's Panels
    @GetMapping
    public ResponseEntity<List<Panel>> getAllPanels() {
        List<Panel> panels = panelRepository.findAll();
        return ResponseEntity.ok(panels);
    }

    // Get one of the team's Panel
    @GetMapping("/{id}")
    public ResponseEntity<Panel> getPanelById(@PathVariable Long id) {
        return panelRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
