package fi.haagahelia.taskmaster.taskmaster.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.soundicly.jnanoidenhanced.jnanoid.NanoIdUtils;

import fi.haagahelia.taskmaster.taskmaster.domain.Invite;
import fi.haagahelia.taskmaster.taskmaster.domain.Team;
import fi.haagahelia.taskmaster.taskmaster.domain.InviteRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;

@Service
public class InviteService {

    private final InviteRepository inviteRepository;
    private final TeamRepository teamRepository;

    public InviteService(InviteRepository inviteRepository, TeamRepository teamRepository) {
        this.inviteRepository = inviteRepository;
        this.teamRepository = teamRepository;
    }

    public String generateInviteCode(Long teamId, int durationHours) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        String nanoId = NanoIdUtils.randomNanoId();
        Date createdAt = new Date();
        Date expiresAt = new Date(createdAt.getTime() + TimeUnit.HOURS.toMillis(durationHours));

        Invite invite = new Invite(nanoId, createdAt, expiresAt, team);
        inviteRepository.save(invite);

        return nanoId;
    }

    public Invite validateInviteCode(String nanoId) {
        Invite invite = inviteRepository.findByNanoId(nanoId)
                .orElseThrow(() -> new RuntimeException("Invite link not found"));

        if (invite.getExpiresAt().before(new Date())) {
            throw new RuntimeException("Invite link has expired");
        }

        return invite;
    }

    public List<Invite> getAllInvites() {
        return inviteRepository.findAll();
    }
}