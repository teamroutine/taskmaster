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
import fi.haagahelia.taskmaster.taskmaster.domain.Panel;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Ticket;
import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.TicketDTO;

@WebMvcTest(BlockRestControllerTest.class)
class BlockRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BlockRepository blockRepository;

    @MockitoBean
    private TicketRepository ticketRepository;

    @MockitoBean
    private PanelRepository panelRepository;

    @MockitoBean
    private TeamRepository teamRepository;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @Test
    void contextLoads() throws Exception {
        mockMvc.perform(get("/api/blocks"))
                .andExpect(status().isOk());
    }
}
