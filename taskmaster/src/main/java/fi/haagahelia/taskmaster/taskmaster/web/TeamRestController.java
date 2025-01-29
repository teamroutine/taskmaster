package fi.haagahelia.taskmaster.taskmaster.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.taskmaster.taskmaster.domain.Team;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
    
    
    }
    

}
