package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.taskmaster.taskmaster.domain.Invite;
import fi.haagahelia.taskmaster.taskmaster.service.InviteService;

import java.util.List;

@RestController
@RequestMapping("/api/invites")
public class InviteRestController {

    @Autowired
    private InviteService inviteService;

    @GetMapping("/generateInvite")
    //When generating a invite teamId and durationHours is passed via url example: /api/invites/generateInvite?teamId=1&durationHours=72
    public ResponseEntity<String> generateInvite(@RequestParam Long teamId, @RequestParam int durationHours) {
        try {
            String inviteLink = inviteService.generateInviteLink(teamId, durationHours);
            return new ResponseEntity<>(inviteLink, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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