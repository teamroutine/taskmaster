package fi.haagahelia.taskmaster.taskmaster.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import fi.haagahelia.taskmaster.taskmaster.config.SecurityConfig;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUser;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Team;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.TeamDTO;
import fi.haagahelia.taskmaster.taskmaster.service.JwtService;

@WebMvcTest(TeamRestController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
class TeamRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketRepository ticketRepository;

    @MockitoBean
    private BlockRepository blockRepository;

    @MockitoBean
    private PanelRepository panelRepository;

    @MockitoBean
    private TeamRepository teamRepository;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @MockitoBean
    private JwtService jwtService;

    private String generateMockJwtToken() {
        return "Bearer mock-jwt-token-more-text-so-its-long-enough";
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllTeams() throws Exception { // When "/api/teams" is not allowed for all the connections, then remember
                                              // to update Authorization for the test
        List<Team> teams = List.of(new Team(1L, "Team1", "Description", new ArrayList<>(), new ArrayList<>(), "user"));
        when(teamRepository.findAll()).thenReturn(teams);

        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamName").value("Team1"));
    }

    @Test
    void testGetTeamById() throws Exception {
        Team team = new Team(1L, "Team1", "Description", new ArrayList<>(), new ArrayList<>(), "user");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        mockMvc.perform(get(
                "/api/teams/1")
                .header("Authorization", generateMockJwtToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName").value("Team1"));
    }

    @Test
    void testCreateTeam() throws Exception {
        TeamDTO teamDTO = new TeamDTO("NewTeam", "NewDescription");
        AppUser user = new AppUser();
        user.setUsername("user");

        Team savedTeam = new Team(1L, "NewTeam", "NewDescription", new ArrayList<>(), List.of(user), "user");

        when(jwtService.getAuthUser(any())).thenReturn("user");
        when(appUserRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(teamRepository.save(any(Team.class))).thenReturn(savedTeam);

        mockMvc.perform(post("/api/teams")
                .header("Authorization", generateMockJwtToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(teamDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.teamName").value("NewTeam"));
    }

    @Test
    void testUpdateTeamData() throws Exception {
        Team existing = new Team(1L, "OldTeam", "OldDescription", new ArrayList<>(), new ArrayList<>(), "user");
        Team updateData = new Team();
        updateData.setTeamName("UpdatedTeam");
        updateData.setDescription("UpdatedDescription");

        when(teamRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(teamRepository.save(any(Team.class))).thenReturn(existing);

        mockMvc.perform(put("/api/teams/1")
                .header("Authorization", generateMockJwtToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName").value("UpdatedTeam"));
    }

    @Test
    void testDeleteTeam() throws Exception {
        Team team = new Team(1L, "ToDelete", "Desc", new ArrayList<>(), new ArrayList<>(), "user");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        mockMvc.perform(delete("/api/teams/1")
                .header("Authorization", generateMockJwtToken()))
                .andExpect(status().isNoContent());

        verify(teamRepository, times(1)).delete(team);
    }

}
