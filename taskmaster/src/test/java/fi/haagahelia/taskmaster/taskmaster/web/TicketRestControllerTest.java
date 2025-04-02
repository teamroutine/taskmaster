package fi.haagahelia.taskmaster.taskmaster.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Block;
import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Ticket;
import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.TicketDTO;

@WebMvcTest(TicketRestController.class)
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
        private AppUserRepository appUserRepository;

        @Test
        void testGetAllTickets() throws Exception {

                // Mock the data to be returned
                Block block = new Block();
                Ticket ticket1 = new Ticket(1L, "Ticket 1", "Description 1", true, LocalDate.now(), block);
                Ticket ticket2 = new Ticket(2L, "Ticket 2", "Description 2", true, LocalDate.now(), block);
                List<Ticket> tickets = Arrays.asList(ticket1, ticket2);

                // Mock the functions of the repository
                when(ticketRepository.findAll()).thenReturn(tickets);

                // Perform a GET request and check the response
                mockMvc.perform(get("/api/tickets"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].ticketName").value("Ticket 1"))
                                .andExpect(jsonPath("$[1].ticketName").value("Ticket 2"));
        }

        @Test
        void testGetTicketById() throws Exception {

                Block block = new Block();
                Ticket ticket1 = new Ticket(1L, "Ticket 1", "Description 1", true, LocalDate.now(), block);
                Ticket ticket2 = new Ticket(2L, "Ticket 2", "Description 2", true, LocalDate.now(), block);

                when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket1));

                mockMvc.perform(get("/api/tickets/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.ticketName").value("Ticket 1"));

                when(ticketRepository.findById(2L)).thenReturn(Optional.of(ticket2));

                mockMvc.perform(get("/api/tickets/2"))
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
                ticketDTO.setBlockId(blockId);

                Ticket ticket = new Ticket();
                ticket.setTicketName("New Ticket");
                ticket.setDescription("New Ticket Description");
                ticket.setBlock(block);

                when(blockRepository.findById(blockId)).thenReturn(Optional.of(block));
                when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

                mockMvc.perform(post("/api/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                                "{\"ticketName\": \"New Ticket\", \"description\": \"New Ticket Description\", \"blockId\": 1}"))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.ticketName").value("New Ticket"))
                                .andExpect(jsonPath("$.description").value("New Ticket Description"));

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
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                                "{\"ticketName\": \"Updated Ticket\", \"description\": \"Updated Description\", \"status\": true}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.ticketName").value("Updated Ticket"))
                                .andExpect(jsonPath("$.description").value("Updated Description"))
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
                ticket.setStatus(true);

                List<Ticket> tickets = Arrays.asList(ticket);

                // Mock the ticketRepository to return the ticket when searched
                when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

                // Perform the DELETE request and check the response
                mockMvc.perform(delete("/api/tickets/{id}", ticketId))
                                .andExpect(status().isNoContent()); // Expect HTTP 204 No Content

                // Verify that after deletion, the findAll returns an empty list
                when(ticketRepository.findAll()).thenReturn(new ArrayList<>()); // Mock an empty list after deletion

                mockMvc.perform(get("/api/tickets"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(0))); // Expect empty list as there are no tickets left
        }
}