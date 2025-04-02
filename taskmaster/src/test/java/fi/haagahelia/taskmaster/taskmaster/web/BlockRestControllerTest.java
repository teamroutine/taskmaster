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

@WebMvcTest(BlockRestController.class)
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
    void testGetAllBlocks() throws Exception {

        Panel panel = new Panel();
        Block block1 = new Block(1L, "Block 1", "Block 1 description", null, panel, new ArrayList<>());
        Block block2 = new Block(2L, "Block 2", "Block 2 description", null, panel, new ArrayList<>());
        List<Block> blocks = Arrays.asList(block1, block2);

        when(blockRepository.findAll()).thenReturn(blocks);

        mockMvc.perform(get("/api/blocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].blockName").value("Block 1"))
                .andExpect(jsonPath("$[1].blockName").value("Block 2"));
    }
}
