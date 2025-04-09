package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import fi.haagahelia.taskmaster.taskmaster.domain.Invite;
import fi.haagahelia.taskmaster.taskmaster.service.InviteService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invites")
public class InviteRestController {

    @Autowired
    private InviteService inviteService;

    @PostMapping("/generateInvite")
    public ResponseEntity<Map<String, String>> generateInvite(@RequestBody Map<String, Object> requestBody) {
        try {
            Long teamId = Long.valueOf(requestBody.get("teamId").toString());
            int durationHours = Integer.parseInt(requestBody.get("durationHours").toString());
            String inviteLink = inviteService.generateInviteLink(teamId, durationHours);
    
            // Return the invite link as a JSON object
            Map<String, String> response = Map.of("inviteLink", inviteLink);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> errorResponse = Map.of("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/validateInvite")
    public ResponseEntity<Invite> validateInvite(@RequestParam String nanoId) {
        try {
            Invite invite = inviteService.validateInviteLink(nanoId);
            return new ResponseEntity<>(invite, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Invite>> getAllInvites() {
        List<Invite> invites = inviteService.getAllInvites();
        return new ResponseEntity<>(invites, HttpStatus.OK);
    }
}