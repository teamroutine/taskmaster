package fi.haagahelia.taskmaster.taskmaster.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import fi.haagahelia.taskmaster.taskmaster.domain.AppUser;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.InviteRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Team;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;

import fi.haagahelia.taskmaster.taskmaster.domain.Invite;

@Service
public class TeamService {

    private final InviteRepository inviteRepository;
    private final TeamRepository teamRepository;
    private final AppUserRepository appUserRepository;

    public TeamService(InviteRepository inviteRepository, TeamRepository teamRepository,
            AppUserRepository appUserRepository) {
        this.inviteRepository = inviteRepository;
        this.teamRepository = teamRepository;
        this.appUserRepository = appUserRepository;
    }

    public void addUserToTeamWithInvite(String inviteCode, String username) {
        Invite invite = inviteRepository.findByNanoId(inviteCode)
                .orElseThrow(() -> new RuntimeException("Invite link not found."));

        if (invite.getExpiresAt().before(new Date())) {
            throw new RuntimeException("Invite link has expired");
        }
        Team team = invite.getTeam();
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (team.getAppUsers().contains(appUser)) {
            throw new RuntimeException("User is already a member of the team.");

        }

        team.getAppUsers().add(appUser);
        teamRepository.save(team);
    }

    public List<Team> getTeamsForAppUser(String username) {
        return appUserRepository.findByUsername(username)
                .map(AppUser::getTeams)
                .orElse(List.of());

    }
}