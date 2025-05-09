package fi.haagahelia.taskmaster.taskmaster.web;

import java.net.Authenticator;
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
import fi.haagahelia.taskmaster.taskmaster.dto.TeamDto;
import fi.haagahelia.taskmaster.taskmaster.service.JwtService;
import fi.haagahelia.taskmaster.taskmaster.service.TeamService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("/api/teams")
@Tag(name = "Teams", description = "Endpoints for managing teams")
public class TeamRestController {

    @Autowired
    private final TeamRepository teamRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private AppUserRepository appUserRepository;

    public TeamRestController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;

    }

    @Operation(summary = "Get all teams", description = "Returns a list of all teams")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all teams")
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

    @Operation(summary = "Get a team by ID", description = "Returns a single team by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team found"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        return teamRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new team", description = "Creates a new team for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Team successfully created"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping
    public ResponseEntity<Team> newTeam(@RequestBody @Valid TeamDto teamDTO, HttpServletRequest request) {
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

    @Operation(summary = "Delete a team", description = "Deletes a team by ID, only if the requester is the creator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Team successfully deleted"),
            @ApiResponse(responseCode = "403", description = "User not authorized to delete this team"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id, Authentication auth) {
        String currentUsername = auth.getName();

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));

        if (!team.getCreatedBy().equals(currentUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the creator can delete this team.");
        }

        teamRepository.delete(team);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Edit a team", description = "Updates the name and description of a team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team successfully updated"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
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

    @Operation(summary = "Get current user's teams", description = "Returns all teams where the authenticated user belongs")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user's teams")
    @GetMapping("/my")
    public ResponseEntity<List<Team>> getUsersOwnTeams(Authentication auth) {
        String username = auth.getName();
        List<Team> teams = teamService.getTeamsForAppUser(username);
        return ResponseEntity.ok(teams);

    }
}
