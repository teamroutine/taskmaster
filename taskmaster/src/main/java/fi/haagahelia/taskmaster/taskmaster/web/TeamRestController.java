package fi.haagahelia.taskmaster.taskmaster.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.taskmaster.taskmaster.domain.AppUser;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Team;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.TeamDTO;
import fi.haagahelia.taskmaster.taskmaster.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin
@RestController
@RequestMapping("/api/teams")
public class TeamRestController {

    @Autowired
    private final TeamRepository teamRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppUserRepository appUserRepository;

    public TeamRestController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;

    }

    // Get all the teams
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeamns() {
        List<Team> teams = teamRepository.findAll();
        teams.forEach(team -> {
            if (team.getPanels() == null) {
                team.setPanels(new ArrayList<>());
            }
        });
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
    public ResponseEntity<Team> newTeam(@RequestBody @Valid TeamDTO teamDTO, HttpServletRequest request) {
        String username = jwtService.getAuthUser(request);
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found" + username));

        Team newTeam = new Team();
        newTeam.setTeamName(teamDTO.getTeamName());
        newTeam.setDescription(teamDTO.getDescription());
        newTeam.setCreatedBy(username);
        newTeam.setAppUsers(List.of(user));
        newTeam.setPanels(new ArrayList<>());
        

        Team savedTeam = teamRepository.save(newTeam);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Block" + id + "can't be deleted, since it doesn't exist."));
        teamRepository.delete(team);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> editTeam(@PathVariable Long id, @RequestBody Team teamData) {

        Team editTeam = teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Team " + id + " can't be edited, since it doesn't exist."));

        editTeam.setTeamName(teamData.getTeamName());
        editTeam.setDescription(teamData.getDescription());

        teamRepository.save(editTeam);

        return ResponseEntity.ok(editTeam);

    }
}
