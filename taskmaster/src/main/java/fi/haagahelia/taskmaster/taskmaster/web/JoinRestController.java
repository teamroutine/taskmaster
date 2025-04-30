package fi.haagahelia.taskmaster.taskmaster.web;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fi.haagahelia.taskmaster.taskmaster.service.TeamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/team/join")
@Tag(name = "Team Join", description = "Endpoint for joining a team with an invite code")
public class JoinRestController {

    private final TeamService teamService;

    public JoinRestController(TeamService teamService) {
        this.teamService = teamService;
    }

    @Operation(summary = "Join a team using an invite code", description = "Authenticated user joins a team using a valid invite code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully added to the team"),
            @ApiResponse(responseCode = "404", description = "Invite code not found or expired"),
            @ApiResponse(responseCode = "400", description = "User already in the team or bad request")
    })
    @PostMapping("/{inviteCode}")
    public ResponseEntity<String> joinTeam(@PathVariable String inviteCode, Principal principal) {
        String username = principal.getName();
        teamService.addUserToTeamWithInvite(inviteCode, username);
        return ResponseEntity.ok("User added to team.");
    }

}
