package fi.haagahelia.taskmaster.taskmaster.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import fi.haagahelia.taskmaster.taskmaster.config.SecurityConfig;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Invite;
import fi.haagahelia.taskmaster.taskmaster.domain.InviteRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;

import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;
import fi.haagahelia.taskmaster.taskmaster.service.InviteService;
import fi.haagahelia.taskmaster.taskmaster.service.JwtService;

@WebMvcTest(InviteRestController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
class InviteRestControllerTest {

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
    private InviteRepository inviteRepository;

    @MockitoBean
    private InviteService inviteService;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @MockitoBean
    private JwtService jwtService;

    private String generateMockJwtToken() {
        return "Bearer mock-jwt-token-more-text-so-its-long-enough";
    }

    @Test
    void testGenerateInvite() throws Exception {

        when(inviteService.generateInviteLink(Mockito.anyLong(), Mockito.anyInt()))
                .thenReturn("mockInviteLink");

        String requestBody = "{\"teamId\": 1, \"durationHours\": 24}";

        mockMvc.perform(post("/api/invites/generateInvite")
                .header("Authorization", generateMockJwtToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.inviteLink").value("mockInviteLink"));
    }

    @Test
    void testValidateInvite_ValidInvite() throws Exception {

        Invite mockInvite = new Invite();
        mockInvite.setNanoId("valid-nano-id");
        when(inviteService.validateInviteLink("valid-nano-id")).thenReturn(mockInvite);

        mockMvc.perform(get("/api/invites/validateInvite")
                .param("nanoId", "valid-nano-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nanoId").value("valid-nano-id"));
    }

    @Test
    void testValidateInvite_InvalidInvite() throws Exception {

        when(inviteService.validateInviteLink("invalid-nano-id")).thenThrow(new RuntimeException("Invite not found"));

        mockMvc.perform(get("/api/invites/validateInvite")
                .param("nanoId", "invalid-nano-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
