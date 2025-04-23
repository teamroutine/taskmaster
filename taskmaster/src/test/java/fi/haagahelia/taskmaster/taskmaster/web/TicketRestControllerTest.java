package fi.haagahelia.taskmaster.taskmaster.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.haagahelia.taskmaster.taskmaster.config.SecurityConfig;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Block;
import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.TagRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Ticket;
import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.TicketDTO;
import fi.haagahelia.taskmaster.taskmaster.service.JwtService;

@WebMvcTest(TicketRestController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
class TicketRestControllerTest {

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
        private TagRepository tagRepository;

        @MockitoBean
        private AppUserRepository appUserRepository;

        @MockitoBean
        private JwtService jwtService;

        private String generateMockJwtToken() {
                return "Bearer mock-jwt-token-more-text-so-its-long-enough"; // Mockattu token
        }

        private String asJsonString(final Object obj) {
                try {
                        return new ObjectMapper().writeValueAsString(obj);
                } catch (Exception e) {
                        throw new RuntimeException(e);
                }
        }

        @Test
        void testGetAllTickets() throws Exception {

                // Mock the data to be returned
                Block block = new Block();
                Ticket ticket1 = new Ticket(1L, "Ticket 1", "Description 1", true, LocalDate.of(2025, 5, 10), LocalDate.now(), block, null);
                Ticket ticket2 = new Ticket(2L, "Ticket 2", "Description 2", true, LocalDate.of(2025, 5, 11), LocalDate.now(), block, null);
                List<Ticket> tickets = Arrays.asList(ticket1, ticket2);

                // Mock the functions of the repository
                when(ticketRepository.findAll()).thenReturn(tickets);

                // Perform a GET request and check the response
                mockMvc.perform(get("/api/tickets")
                                .header("Authorization", generateMockJwtToken()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].ticketName").value("Ticket 1"))
                                .andExpect(jsonPath("$[1].ticketName").value("Ticket 2"));
        }

        @Test
        void testGetTicketById() throws Exception {

                Block block = new Block();
                Ticket ticket1 = new Ticket(1L, "Ticket 1", "Description 1", true,LocalDate.of(2025, 5, 10), LocalDate.now(), block, null);
                Ticket ticket2 = new Ticket(2L, "Ticket 2", "Description 2", true, LocalDate.of(2025, 5, 11), LocalDate.now(), block, null);

                when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket1));

                mockMvc.perform(get("/api/tickets/1")
                                .header("Authorization", generateMockJwtToken()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.ticketName").value("Ticket 1"));

                when(ticketRepository.findById(2L)).thenReturn(Optional.of(ticket2));

                mockMvc.perform(get("/api/tickets/2")
                                .header("Authorization", generateMockJwtToken()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.ticketName").value("Ticket 2"));
        }

        @Test
        void testCreateTicket() throws Exception {

                Long blockId = 1L;
                Block block = new Block();
                block.setBlockId(blockId);

                TicketDTO ticketDTO = new TicketDTO();
                ticketDTO.setTicketName("New Ticket");
                ticketDTO.setDescription("New Ticket Description");
                ticketDTO.setDueDate(LocalDate.of(2025, 6, 10));
                ticketDTO.setBlockId(blockId);

                Ticket ticket = new Ticket();
                ticket.setTicketName("New Ticket");
                ticket.setDescription("New Ticket Description");
                ticket.setDueDate(LocalDate.of(2025, 6, 10));
                ticket.setBlock(block);

                when(blockRepository.findById(blockId)).thenReturn(Optional.of(block));
                when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

                mockMvc.perform(post("/api/tickets")
                                .header("Authorization", generateMockJwtToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                                "{\"ticketName\": \"New Ticket\", \"description\": \"New Ticket Description\",\"dueDate\": \"2025-06-10\", \"blockId\": 1}"))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.ticketName").value("New Ticket"))
                                .andExpect(jsonPath("$.description").value("New Ticket Description"))
                                .andExpect(jsonPath("$.dueDate").value("2025-06-10"));

        }

        @Test
        void testUpdateTicket() throws Exception {

                Long ticketId = 1L;
                Ticket ticket = new Ticket();
                ticket.setTicketId(ticketId);
                ticket.setTicketName("Old Ticket");
                ticket.setDescription("Old Description");
                ticket.setStatus(true);

                Ticket ticketData = new Ticket();
                ticketData.setTicketName("Updated Ticket");
                ticketData.setDescription("Updated Description");
                ticketData.setStatus(true);

                when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
                when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

                mockMvc.perform(put("/api/tickets/{id}", ticketId)
                                .header("Authorization", generateMockJwtToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(ticketData)))
                                .andExpect(jsonPath("$.ticketName").value(ticketData.getTicketName()))
                                .andExpect(jsonPath("$.description").value(ticketData.getDescription()))
                                .andExpect(jsonPath("$.status").value(true));

                // Make sure that all the fields are correct
                assertEquals("Updated Ticket", ticket.getTicketName());
                assertEquals("Updated Description", ticket.getDescription());
                assertEquals(true, ticket.getStatus());

        }

        @Test
        void testDeleteTicket() throws Exception {

                Long ticketId = 1L;
                Ticket ticket = new Ticket();
                ticket.setTicketId(ticketId);
                ticket.setTicketName("Ticket to be deleted");
                ticket.setDescription("Description to be deleted");
                ticket.setDueDate(LocalDate.of(2025, 6, 5));
                ticket.setStatus(true);

                when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

                mockMvc.perform(delete("/api/tickets/{id}", ticketId)
                                .header("Authorization", generateMockJwtToken()))
                                .andExpect(status().isNoContent());

                when(ticketRepository.findAll()).thenReturn(new ArrayList<>());

                mockMvc.perform(get("/api/tickets")
                                .header("Authorization", generateMockJwtToken()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(0)));
        }
}