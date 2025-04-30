package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import fi.haagahelia.taskmaster.taskmaster.domain.Invite;
import fi.haagahelia.taskmaster.taskmaster.service.InviteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invites")
@Tag(name = "Invites", description = "Endpoints for managing team invite links")
public class InviteRestController {

    @Autowired
    private InviteService inviteService;

    @Operation(summary = "Generate an invite link", description = "Creates a time-limited invite code for a team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Invite successfully generated"),
            @ApiResponse(responseCode = "500", description = "Server error during invite generation")
    })
    @PostMapping("/generateInvite")
    public ResponseEntity<Map<String, String>> generateInvite(@RequestBody Map<String, Object> requestBody) {
        try {
            Long teamId = Long.valueOf(requestBody.get("teamId").toString());
            int durationHours = Integer.parseInt(requestBody.get("durationHours").toString());
            String inviteCode = inviteService.generateInviteCode(teamId, durationHours);

            // Return the invite link as a JSON object
            Map<String, String> response = Map.of("inviteCode", inviteCode);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> errorResponse = Map.of("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Validate an invite code", description = "Checks if the provided invite code is valid and not expired")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invite is valid"),
            @ApiResponse(responseCode = "404", description = "Invite not found or expired")
    })
    @GetMapping("/validate/{nanoId}")
    public ResponseEntity<Invite> validateInvite(@PathVariable String nanoId) {
        try {
            Invite invite = inviteService.validateInviteCode(nanoId);
            return new ResponseEntity<>(invite, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all invites", description = "Returns a list of all invite codes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all invites")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Invite>> getAllInvites() {
        List<Invite> invites = inviteService.getAllInvites();
        return new ResponseEntity<>(invites, HttpStatus.OK);
    }
}