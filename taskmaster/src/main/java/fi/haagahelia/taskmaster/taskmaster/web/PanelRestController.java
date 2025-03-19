package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.taskmaster.taskmaster.domain.Panel;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin
@RestController
@RequestMapping("/api/panels") // Is (api/teams/{teamId}/panels) better?
public class PanelRestController {

    @Autowired
    private final PanelRepository panelRepository;

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

    // Create a new panel
    @PostMapping
    public ResponseEntity<Panel> newPanel(@RequestBody @NonNull Panel newPanel) {
        Panel savedPanel = panelRepository.save(newPanel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPanel);
    }

    // Edit one panel
    @PutMapping("/{id}")
    public ResponseEntity<Panel> editPanel(@PathVariable Long id,
            @RequestBody Panel panelData) {
        Panel editPanel = panelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Panel " + id + "can't be edited, since it doesn't exist."));

        editPanel.setPanelName(panelData.getPanelName());
        editPanel.setDescription(panelData.getDescription());

        panelRepository.save(panelData);

        return ResponseEntity.ok(editPanel);
    }

    // Delete a panel
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePanel(@PathVariable Long id) {
        Panel panel = panelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Panel " + id + " can't be deleted, since it doesn't exist."));

        panelRepository.delete(panel);
        return ResponseEntity.noContent().build();
    }
}
