package fi.haagahelia.taskmaster.taskmaster.web;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

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
import fi.haagahelia.taskmaster.taskmaster.domain.AppUser;
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

    private String generateMockJwtToken() {
        return "Bearer mock-jwt-token-more-text-so-its-long-enough"; // Mockattu token
    }

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
        String email = "testemail";
        String firstName = "testfirstname";
        String lastName = "testlastname";

        LoginUserDto loginDto = new LoginUserDto(username, password);
        AppUser mockUserDetails = new AppUser();
        mockUserDetails.setUsername(username);
        mockUserDetails.setPassword(password);
        mockUserDetails.setEmail(email);
        mockUserDetails.setFirstName(firstName);
        mockUserDetails.setLastName(lastName);

        AccessTokenPayloadDto mockToken = new AccessTokenPayloadDto("fake-jwt-token", mockExpiresAt);

        // Mock the behavior
        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        when(jwtService.getAccessToken(username, email, firstName, lastName)).thenReturn(mockToken);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null); // <-- tämä korjaa virheen

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("fake-jwt-token"));
    }

    @Test
    void testUnsuccesfulAuthenticationReturnsForbiddenStatus() throws Exception {
        String username = "testuser";
        String password = "wrongpassword";

        LoginUserDto loginUserDto = new LoginUserDto(username, password);

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(
                        new org.springframework.security.authentication.BadCredentialsException(("Bad credentials")));

        mockMvc.perform(post("/api/auth/login")
                .header("Authorization", generateMockJwtToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginUserDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginWithMissingRequestBodyReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testLoginWithValidCredentialsReturnsToken() throws Exception {
        String username = "testuser";
        String password = "testpassword";
        String email = "testemail";
        String firstName = "testfirstname";
        String lastName = "testlastname";

        LoginUserDto loginDto = new LoginUserDto(username, password);
        AccessTokenPayloadDto mockToken = new AccessTokenPayloadDto("valid-jwt-token", mockExpiresAt);

        AppUser mockUserDetails = new AppUser();
        mockUserDetails.setUsername(username);
        mockUserDetails.setPassword(password);
        mockUserDetails.setEmail(email);
        mockUserDetails.setFirstName(firstName);
        mockUserDetails.setLastName(lastName);

        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);

        // Mock authentication and token generation
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null);
        when(jwtService.getAccessToken(username, email, firstName, lastName)).thenReturn(mockToken);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("valid-jwt-token"))
                .andExpect(jsonPath("$.expiresAt").value(mockExpiresAt.toString()));
    }
}