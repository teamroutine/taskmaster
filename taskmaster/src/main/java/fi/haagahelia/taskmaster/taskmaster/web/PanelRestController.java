package fi.haagahelia.taskmaster.taskmaster.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.taskmaster.taskmaster.domain.Panel;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Team;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.PanelDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("/api/panels")
@Tag(name = "Panels", description = "Endpoints for managing panels within teams")
public class PanelRestController {

        @Autowired
        private final PanelRepository panelRepository;
        private final TeamRepository teamRepository;

        public PanelRestController(PanelRepository panelRepository, TeamRepository teamRepository) {
                this.panelRepository = panelRepository;
                this.teamRepository = teamRepository;
        }

        @Operation(summary = "Get all panels", description = "Returns a list of all panels")
        @ApiResponse(responseCode = "200", description = "Successfully retrieved panels")
        @GetMapping
        public ResponseEntity<List<Panel>> getAllPanels() {
                List<Panel> panels = panelRepository.findAll();
                return ResponseEntity.ok(panels);
        }

        @Operation(summary = "Get panel by ID", description = "Fetch a panel by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Panel found"),
                        @ApiResponse(responseCode = "404", description = "Panel not found")
        })
        @GetMapping("/{id}")
        public ResponseEntity<Panel> getPanelById(@PathVariable Long id) {
                return panelRepository.findById(id)
                                .map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @Operation(summary = "Create a new panel", description = "Creates a new panel under a specific team")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Panel successfully created"),
                        @ApiResponse(responseCode = "404", description = "Team not found")
        })
        @PostMapping
        public ResponseEntity<Panel> newPanel(@RequestBody @NonNull PanelDto panelDto) {

                Team team = teamRepository.findById(panelDto.getTeamId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));

                Panel newPanel = new Panel();
                newPanel.setPanelName(panelDto.getPanelName());
                newPanel.setDescription(panelDto.getDescription());
                newPanel.setTeam(team);

                Panel savedPanel = panelRepository.save(newPanel);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedPanel);
        }

        @Operation(summary = "Edit an existing panel", description = "Update the name and description of a panel")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Panel successfully updated"),
                        @ApiResponse(responseCode = "404", description = "Panel not found")
        })
        @PutMapping(value = "/{id}")
        public ResponseEntity<Panel> editPanel(@PathVariable Long id,
                        @RequestBody Panel panelData) {
                Panel editPanel = panelRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Panel " + id + " can't be edited, since it doesn't exist."));

                editPanel.setPanelName(panelData.getPanelName());
                editPanel.setDescription(panelData.getDescription());

                panelRepository.save(editPanel);

                return ResponseEntity.ok(editPanel);
        }

        @Operation(summary = "Delete a panel", description = "Deletes a panel by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Panel successfully deleted"),
                        @ApiResponse(responseCode = "404", description = "Panel not found")
        })
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
