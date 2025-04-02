package fi.haagahelia.taskmaster.taskmaster.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import fi.haagahelia.taskmaster.taskmaster.domain.Block;
import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.web.BlockRestController;

@WebMvcTest(BlockRestController.class)
class BlockRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BlockRepository blockRepository;

    @Test
    void testGetAllBlocks() throws Exception {

        Block block1 = new Block();
        block1.setBlockId(1L);
        block1.setBlockName("Block 1");

        Block block2 = new Block();
        block2.setBlockId(2L);
        block2.setBlockName("Block 2");

        List<Block> blocks = Arrays.asList(block1, block2);

        when(blockRepository.findAll()).thenReturn(blocks);

        mockMvc.perform(get("/api/blocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].blockName").value("Block 1"))
                .andExpect(jsonPath("$[1].blockName").value("Block 2"));
    }
}
