package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Panel;
import fi.haagahelia.taskmaster.taskmaster.domain.Block;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Block;
import fi.haagahelia.taskmaster.taskmaster.dto.BlockDto;
import io.micrometer.common.lang.NonNull;

import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin
@RestController
@RequestMapping("/api/blocks")
public class BlockRestController {
    private final BlockRepository blockRepository;
    private final PanelRepository panelRepository;

    @Autowired
    public BlockRestController(BlockRepository blockRepository, PanelRepository panelRepository) {
        this.blockRepository = blockRepository;
        this.panelRepository = panelRepository;
    }

    // Get all blocks
    @GetMapping
    public ResponseEntity<List<Block>> getAllBlocks() {
        List<Block> blocks = blockRepository.findAll();
        return ResponseEntity.ok(blocks);
    }

    // Create a new block
    @PostMapping
    public ResponseEntity<Block> newBlock(@RequestBody @NonNull BlockDto blockDto) {
        Panel panel = panelRepository.findById(blockDto.getPanelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Panel not found"));

        Block newBlock = new Block();
        newBlock.setBlockName(blockDto.getBlockName());
        newBlock.setDescription(blockDto.getDescription());
        newBlock.setHighlightColor(blockDto.getHighlightColor());
        newBlock.setPanel(panel);

        Block savedBlock = blockRepository.save(newBlock);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBlock);
    }

    // Edit block
    @PutMapping("/{id}")
    public ResponseEntity<Block> editBlock(@PathVariable Long id, @RequestBody Block blockData) {
        Block editBlock = blockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Block " + id + " can't be edited, since it doesn't exist."));

        editBlock.setBlockName(blockData.getBlockName());
        editBlock.setDescription(blockData.getDescription());
        editBlock.setHighlightColor(blockData.getHighlightColor());
        editBlock.setPanel(blockData.getPanel());
        editBlock.setTickets(blockData.getTickets());

        blockRepository.save(editBlock);

        return ResponseEntity.ok(editBlock);
    }

    // Delete a block
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        Block block = blockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Block " + id + " can't be deleted, since it doesn't exist."));

        blockRepository.delete(block);
        return ResponseEntity.noContent().build();
    }

}
