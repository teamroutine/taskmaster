package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Block;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/blocks")
public class BlockRestController {
private final BlockRepository blockRepository;

@Autowired
public BlockRestController(BlockRepository blockRepository) {
    this.blockRepository = blockRepository;
}

@GetMapping
public ResponseEntity<List<Block>> getAllBlocks(){
    List<Block> blocks = blockRepository.findAll();
    return ResponseEntity.ok(blocks);
}
}
