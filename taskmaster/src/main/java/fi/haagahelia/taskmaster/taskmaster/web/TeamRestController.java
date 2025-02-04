package fi.haagahelia.taskmaster.taskmaster.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.taskmaster.taskmaster.domain.Team;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import io.micrometer.common.lang.NonNull;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin
@RestController
@RequestMapping("/api/teams")
public class TeamRestController {

    @Autowired
    private final TeamRepository teamRepository;

    public TeamRestController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;

    }

    // Get all the teams
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeamns() {
        List<Team> teams = teamRepository.findAll();
        return ResponseEntity.ok(teams);
    }

    // Get one team
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        return teamRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED, reason = "New team created")
    public Team newTeam(@RequestBody @NonNull Team newTeam) {
        return teamRepository.save(newTeam);
    }

}
