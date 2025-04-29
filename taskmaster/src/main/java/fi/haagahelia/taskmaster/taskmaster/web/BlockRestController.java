package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Panel;
import fi.haagahelia.taskmaster.taskmaster.domain.Block;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;

import fi.haagahelia.taskmaster.taskmaster.dto.BlockDto;
import io.micrometer.common.lang.NonNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin
@RestController
@RequestMapping("/api/blocks")
@Tag(name = "Blocks", description = "Endpoints for managing blocks within panels")
public class BlockRestController {
    private final BlockRepository blockRepository;
    private final PanelRepository panelRepository;

    @Autowired
    public BlockRestController(BlockRepository blockRepository, PanelRepository panelRepository) {
        this.blockRepository = blockRepository;
        this.panelRepository = panelRepository;
    }

    // Get all blocks
    @Operation(summary = "Get all blocks", description = "Returns a list of all blocks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved blocks")
    })
    @GetMapping
    public ResponseEntity<List<Block>> getAllBlocks() {
        List<Block> blocks = blockRepository.findAll();
        return ResponseEntity.ok(blocks);
    }

    // Get block by id
    @Operation(summary = "Get a block by ID", description = "Fetch a single block using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Block found"),
            @ApiResponse(responseCode = "404", description = "Block not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Block> getBlockById(@PathVariable Long id) {
        return blockRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new block
    @Operation(summary = "Create a new block", description = "Adds a new block to the specified panel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Block successfully created"),
            @ApiResponse(responseCode = "404", description = "Panel not found"),
            @ApiResponse(responseCode = "409", description = "Block with the same name already exists in this panel")
    })
    @PostMapping
    public ResponseEntity<Block> newBlock(@RequestBody @NonNull BlockDto blockDto) {
        Panel panel = panelRepository.findById(blockDto.getPanelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Panel not found"));

        List<Block> existingBlock = blockRepository.findByBlockNameAndPanel_PanelId(blockDto.getBlockName(),
                blockDto.getPanelId());
        if (!existingBlock.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Block with the same name already exists in this panel.");
        }

        Integer maxSortOrder = blockRepository
                .findMaxSortOrderByPanelId(blockDto.getPanelId())
                .orElse(-1);

        Block newBlock = new Block();
        newBlock.setBlockName(blockDto.getBlockName());
        newBlock.setDescription(blockDto.getDescription());
        newBlock.setHighlightColor(blockDto.getHighlightColor());
        newBlock.setPanel(panel);
        newBlock.setSortOrder(maxSortOrder + 1);

        Block savedBlock = blockRepository.save(newBlock);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBlock);
    }

    // Edit block
    @Operation(summary = "Edit an existing block", description = "Updates the details of an existing block")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Block successfully updated"),
            @ApiResponse(responseCode = "404", description = "Block not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Block> editBlock(@PathVariable Long id, @RequestBody Block blockData) {
        Block editBlock = blockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Block " + id + " can't be edited, since it doesn't exist."));

        editBlock.setBlockName(blockData.getBlockName());
        editBlock.setDescription(blockData.getDescription());
        editBlock.setHighlightColor(blockData.getHighlightColor());
        editBlock.setTickets(blockData.getTickets());

        blockRepository.save(editBlock);

        return ResponseEntity.ok(editBlock);
    }

    // Delete a block
    @Operation(summary = "Delete a block", description = "Removes a block unless it is the 'Done' block")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Block successfully deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden to delete 'Done' block"),
            @ApiResponse(responseCode = "404", description = "Block not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        Block block = blockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Block " + id + " can't be deleted, since it doesn't exist."));

        // Don't let user delete done block
        if ("Done".equalsIgnoreCase(block.getBlockName())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "The 'Done' block cannot be deleted.");
        }

        blockRepository.delete(block);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reorder blocks", description = "Changes the sort order of blocks in a panel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Blocks reordered successfully"),
            @ApiResponse(responseCode = "404", description = "Panel or Block not found"),
            @ApiResponse(responseCode = "400", description = "Block does not belong to the specified panel")
    })
    @PutMapping("/reorder")
    public ResponseEntity<Void> reorderBlocks(@RequestBody @NonNull List<BlockDto> blocks,
            @RequestParam Long panelId) {

        Panel panel = panelRepository.findById(panelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Panel not found"));

        for (BlockDto blockDto : blocks) {
            Block block = blockRepository.findById(blockDto.getBlockId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Block not found: " + blockDto.getBlockId()));

            if (!block.getPanel().getPanelId().equals(panelId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Block " + blockDto.getBlockId() + " does not belong to panel " + panelId);
            }
        }

        int index = 0;
        for (BlockDto blockDto : blocks) {
            Block block = blockRepository.findById(blockDto.getBlockId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Block not found: " + blockDto.getBlockId()));

            block.setSortOrder(index++);
            blockRepository.save(block);
        }

        return ResponseEntity.noContent().build();
    }

}
