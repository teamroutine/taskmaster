package fi.haagahelia.taskmaster.taskmaster.web;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fi.haagahelia.taskmaster.taskmaster.service.TeamService;

@RestController
@RequestMapping("/api/team/join")
public class JoinRestController {

    private final TeamService teamService;

    public JoinRestController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/{inviteCode}")
    public ResponseEntity<String> joinTeam(@PathVariable String inviteCode, Principal principal) {
        String username = principal.getName();
        teamService.addUserToTeamWithInvite(inviteCode, username);
        return ResponseEntity.ok("User added to team.");
    }

}
