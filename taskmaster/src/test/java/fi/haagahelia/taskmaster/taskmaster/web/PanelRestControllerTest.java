package fi.haagahelia.taskmaster.taskmaster.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.haagahelia.taskmaster.taskmaster.config.SecurityConfig;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;

import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Panel;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Team;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;

import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;

import fi.haagahelia.taskmaster.taskmaster.service.JwtService;

@WebMvcTest(PanelRestController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
class PanelRestControllerTest {

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

    private String generateMockJewString() {
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
    void testGetAllPanels() throws Exception {
        Team team = new Team();
        Panel panel1 = new Panel(1L, "Panel 1", "Panel 1 description", team, new ArrayList<>());
        Panel panel2 = new Panel(2L, "Panel 2", "Panel 1 description", team, new ArrayList<>());
        List<Panel> panels = Arrays.asList(panel1, panel2);

        when(panelRepository.findAll()).thenReturn(panels);

        mockMvc.perform(get("/api/panels")
                .header("Authorization", generateMockJewString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].panelName").value("Panel 1"))
                .andExpect(jsonPath("$[1].panelName").value("Panel 2"));

    }

    @Test
    void testGetPanelById() throws Exception {
        Team team = new Team();
        Panel panel1 = new Panel(1L, "Panel 1", "Panel 1 description", team, new ArrayList<>());
        Panel panel2 = new Panel(1L, "Panel 2", "Panel 1 description", team, new ArrayList<>());

        when(panelRepository.findById(1L)).thenReturn(Optional.of(panel1));

        mockMvc.perform(get("/api/panels/1")
                .header("Authorization", generateMockJewString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.panelName").value("Panel 1"));

        when(panelRepository.findById(2L)).thenReturn(Optional.of(panel2));

        mockMvc.perform(get("/api/panels/2")
                .header("Authorization", generateMockJewString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.panelName").value("Panel 2"));
    }

    @Test
    void testCreateTicket() throws Exception {

        Long teamId = 1L;
        Team team = new Team();
        team.setTeamId(teamId);

        Panel panel = new Panel();
        panel.setPanelName("Panel 1");
        panel.setDescription("Description for Panel 1");
        panel.setTeam(team);
        panel.setBlocks(new ArrayList<>());

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(panelRepository.save(any(Panel.class))).thenReturn(panel);

        mockMvc.perform(post("/api/panels")
                .header("Authorization", generateMockJewString())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"panelName\": \"Panel 1\", \"description\": \"Description for Panel 1\", \"teamId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.panelName").value("Panel 1"))
                .andExpect(jsonPath("$.description").value("Description for Panel 1"));

    }

    @Test
    void testUpdatePanel() throws Exception {

        Long teamId = 1L;
        Team team = new Team();
        team.setTeamId(teamId);

        Long panelId = 1L;
        Panel panel = new Panel();
        panel.setPanelId(panelId);
        panel.setPanelName("Panel 1");
        panel.setDescription("Description for Panel 1");
        panel.setTeam(team);
        panel.setBlocks(new ArrayList<>());

        Panel updatedPanel = new Panel();
        updatedPanel.setPanelName("Updated name for Panel 1");
        updatedPanel.setDescription("Updated description for Panel 1");

        when(panelRepository.findById(panelId)).thenReturn(Optional.of(panel));
        when(panelRepository.save(any(Panel.class))).thenReturn(panel);

        mockMvc.perform(put("/api/panels/{id}", panelId)
                .header("Authorization", generateMockJewString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedPanel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.panelName").value(updatedPanel.getPanelName()))
                .andExpect(jsonPath("$.description").value(updatedPanel.getDescription()));

        assertEquals("Updated name for Panel 1", panel.getPanelName());
        assertEquals("Updated description for Panel 1", panel.getDescription());
        assertEquals(1L, panel.getPanelId());

    }

    @Test
    void testDeletePanel() throws Exception {

        Long teamId = 1L;
        Team team = new Team();
        team.setTeamId(teamId);

        Long panelId = 1L;
        Panel panel1 = new Panel();
        panel1.setPanelId(panelId);
        panel1.setPanelName("Panel 1");
        panel1.setDescription("Description for Panel 1");
        panel1.setTeam(team);
        panel1.setBlocks(new ArrayList<>());

        List<Panel> panels = Arrays.asList(panel1);

        when(panelRepository.findById(panelId)).thenReturn(Optional.of(panel1));
        when(panelRepository.findAll()).thenReturn(panels);

        mockMvc.perform(delete("/api/panels/{id}", panelId)
                .header("Authorization", generateMockJewString()))
                .andExpect(status().isNoContent());

        Mockito.verify(panelRepository).delete(panel1);
    }
}