package fi.haagahelia.taskmaster.taskmaster.web;

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
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.RegisterUserDto;
import fi.haagahelia.taskmaster.taskmaster.service.AppUserService;
import fi.haagahelia.taskmaster.taskmaster.service.JwtService;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        void testCreateAppUserWhenUsernameIsAvailable() throws Exception {
                RegisterUserDto validUserDto = new RegisterUserDto();
                validUserDto.setFirstName("Test");
                validUserDto.setLastName("User");
                validUserDto.setEmail("test@example.com");
                validUserDto.setPhone("+358501234567");
                validUserDto.setUsername("newuser");
                validUserDto.setPassword("password123");

                when(appUserRepository.findByUsername("newuser")).thenReturn(Optional.empty());

                AppUser createdUser = new AppUser(
                                1L,
                                "Test",
                                "User",
                                "test@example.com",
                                "+358501234567",
                                "newuser",
                                "encodedpassword",
                                null);

                when(appUserService.registerUser(any(RegisterUserDto.class))).thenReturn(createdUser);

                mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(validUserDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.username").value("newuser"))
                                .andExpect(jsonPath("$.firstName").value("Test"));
        }

        @Test
        void testCreateAppUserWhenUsernameIsTaken() throws Exception {
                RegisterUserDto validUserDto = new RegisterUserDto();
                validUserDto.setFirstName("Test");
                validUserDto.setLastName("User");
                validUserDto.setEmail("test@example.com");
                validUserDto.setPhone("+358501234567");
                validUserDto.setUsername("newuser");
                validUserDto.setPassword("password123");

                AppUser existingUser = new AppUser();
                existingUser.setUsername("newuser");

                when(appUserRepository.findByUsername("newuser")).thenReturn(Optional.of(existingUser));

                mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(validUserDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(status().reason("This username is already taken. Choose another one"));
        }

        @Test
        void testUpdateAppUserDataWhenUsernameIsAvailable() throws Exception {
                Long userId = 1L;
                AppUser originalUser = new AppUser();
                originalUser.setId(userId);
                originalUser.setFirstName("Old");
                originalUser.setLastName("Name");
                originalUser.setEmail("old@example.com");
                originalUser.setPhone("123456");
                originalUser.setUsername("oldusername");
                originalUser.setPassword("oldpassword");

                AppUser updatedUser = new AppUser();
                updatedUser.setFirstName("New");
                updatedUser.setLastName("Name");
                updatedUser.setEmail("new@example.com");
                updatedUser.setPhone("654321");
                updatedUser.setUsername("newusername");
                updatedUser.setPassword("newpassword");

                when(appUserRepository.findById(userId)).thenReturn(Optional.of(originalUser));
                when(appUserRepository.save(any(AppUser.class))).thenReturn(originalUser);

                mockMvc.perform(put("/api/users/{id}", userId)
                                .header("Authorization", generateMockJwtToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(updatedUser)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.firstName").value(updatedUser.getFirstName()))
                                .andExpect(jsonPath("$.lastName").value(updatedUser.getLastName()))
                                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                                .andExpect(jsonPath("$.phone").value(updatedUser.getPhone()))
                                .andExpect(jsonPath("$.username").value(updatedUser.getUsername()));

                assertEquals("New", originalUser.getFirstName());
                assertEquals("Name", originalUser.getLastName());
                assertEquals("new@example.com", originalUser.getEmail());
                assertEquals("654321", originalUser.getPhone());
                assertEquals("newusername", originalUser.getUsername());

        }

        @Test
        void testUpdateAppUserUsernameConflict() throws Exception {
                Long userId = 1L;

                AppUser originalUser = new AppUser();
                originalUser.setId(userId);
                originalUser.setUsername("originalUser");
                originalUser.setFirstName("Original");
                originalUser.setLastName("User");
                originalUser.setEmail("original@example.com");
                originalUser.setPhone("123456");
                originalUser.setPassword("password");

                AppUser updatedUser = new AppUser();
                updatedUser.setUsername("existingUser");
                updatedUser.setFirstName("Updated");
                updatedUser.setLastName("User");
                updatedUser.setEmail("updated@example.com");
                updatedUser.setPhone("654321");
                updatedUser.setPassword("newpassword");

                AppUser existingUser = new AppUser();
                existingUser.setId(2L); // eri käyttäjä
                existingUser.setUsername("existingUser");

                when(appUserRepository.findById(userId)).thenReturn(Optional.of(originalUser));
                when(appUserRepository.findByUsername("existingUser")).thenReturn(Optional.of(existingUser));

                mockMvc.perform(put("/api/users/{id}", userId)
                                .header("Authorization", generateMockJwtToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(updatedUser)))
                                .andExpect(status().isConflict()); // <-- 409

                assertEquals("originalUser", originalUser.getUsername());
        }

        @Test
        void testDeleteAppUser() throws Exception {
                Long userId = 1L;

                AppUser appUser = new AppUser();
                appUser.setId(userId);
                appUser.setUsername("to-be-deleted");

                when(appUserRepository.findById(userId)).thenReturn(Optional.of(appUser));

                mockMvc.perform(delete("/api/users/{id}", userId)
                                .header("Authorization", generateMockJwtToken()))
                                .andExpect(status().isNoContent());

                verify(appUserRepository).findById(userId);
                verify(appUserRepository).delete(appUser);
        }

}