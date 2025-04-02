package fi.haagahelia.taskmaster.taskmaster.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.haagahelia.taskmaster.taskmaster.domain.*;
import fi.haagahelia.taskmaster.taskmaster.dto.BlockDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BlockRestControllerTest {

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private PanelRepository panelRepository;

    @Mock
    private BlockRestController blockRestController;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(blockRestController).build();
    }

    @Test
    void getAllBlocks_ShouldReturnListOfBlocks() throws Exception {
        Block block1 = new Block();
        block1.setBlockId(1L);
        block1.setBlockName("Block 1");

        Block block2 = new Block();
        block2.setBlockId(2L);
        block2.setBlockName("Block 2");

        when(blockRepository.findAll()).thenReturn(Arrays.asList(block1, block2));

        mockMvc.perform(get("/api/blocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].blockName").value("Block 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].blockName").value("Block 2"));
    }
}
