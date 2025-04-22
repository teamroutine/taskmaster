package fi.haagahelia.taskmaster.taskmaster.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.haagahelia.taskmaster.taskmaster.config.SecurityConfig;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUser;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Team;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.RegisterUserDto;
import fi.haagahelia.taskmaster.taskmaster.service.AppUserService;
import fi.haagahelia.taskmaster.taskmaster.service.JwtService;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
class UserRestControllerTest {

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
    private AppUserService appUserService;

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
    void testGetAllUsers() throws Exception {

        AppUser user1 = new AppUser(1L, "Test", "Person", "test@test.com", "+358501111111", "testuser",
                "password", null);

        AppUser user2 = new AppUser(2L, "Mock", "User", "mock@mock.com", "+358502222222", "mockuser",
                "password", null);

        List<AppUser> appUsers = Arrays.asList(user1, user2);

        when(appUserRepository.findAll()).thenReturn(appUsers);

        mockMvc.perform(get("/api/users")
                .header("Authorization", generateMockJwtToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Test"))
                .andExpect(jsonPath("$[1].firstName").value("Mock"));

    }

    @Test
    void testGetUserById() throws Exception {

        AppUser user1 = new AppUser(1L, "Test", "Person", "test@test.com", "+358501111111", "testuser",
                "password", null);

        AppUser user2 = new AppUser(2L, "Mock", "User", "mock@mock.com", "+358502222222", "mockuser",
                "password", null);

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user1));

        mockMvc.perform(get("/api/users/1")
                .header("Authorization", generateMockJwtToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));

        when(appUserRepository.findById(2L)).thenReturn(Optional.of(user2));

        mockMvc.perform(get("/api/users/2")
                .header("Authorization", generateMockJwtToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("mockuser"));
    }

    @Test
    void testCreateUser() throws Exception {

        RegisterUserDto user1 = new RegisterUserDto();
        user1.setFirstName("Test");
        user1.setLastName("Person");
        user1.setEmail("test@test.com");
        user1.setPhone("+358501111111");
        user1.setUsername("testuser");
        user1.setPassword("password");

        AppUser createdUser = new AppUser(1L, "Test", "Person", "test@test.com", "+358501111111", "testuser",
                "password", null);
        when(appUserService.registerUser(any(RegisterUserDto.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/users")
                .header("Authorization", generateMockJwtToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.firstName").value("Test"));

    }
}
