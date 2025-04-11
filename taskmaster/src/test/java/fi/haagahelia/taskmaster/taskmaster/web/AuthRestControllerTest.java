package fi.haagahelia.taskmaster.taskmaster.web;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.haagahelia.taskmaster.taskmaster.config.SecurityConfig;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.InviteRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.AccessTokenPayloadDto;
import fi.haagahelia.taskmaster.taskmaster.dto.LoginUserDto;
import fi.haagahelia.taskmaster.taskmaster.service.AppUserService;
import fi.haagahelia.taskmaster.taskmaster.service.InviteService;
import fi.haagahelia.taskmaster.taskmaster.service.JwtService;

@WebMvcTest(AuthRestController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
class AuthRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AppUserService appUserService;

    @MockitoBean
    private InviteService inviteService;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @MockitoBean
    private TeamRepository teamRepository;

    @MockitoBean
    private InviteRepository inviteRepository;

    @MockitoBean
    private TicketRepository ticketRepository;

    @MockitoBean
    private PanelRepository panelRepository;

    @MockitoBean
    private BlockRepository blockRepository;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthRestController authRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authRestController).build();
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Instant mockExpiresAt = Instant.now().plusSeconds(8 * 60 * 60);

    @Test
    void testSuccessfulAuthenticationReturnsOkStatus() throws Exception {
        String username = "testuser";
        String password = "testpassword";

        LoginUserDto loginDto = new LoginUserDto(username, password);
        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                username, password, new ArrayList<>());

        AccessTokenPayloadDto mockToken = new AccessTokenPayloadDto("fake-jwt-token", mockExpiresAt);

        // Mock the behavior
        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        when(jwtService.getAccessToken(username)).thenReturn(mockToken);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null); // <-- tämä korjaa virheen

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("fake-jwt-token"));
    }
}