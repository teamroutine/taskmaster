package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Block;
import fi.haagahelia.taskmaster.taskmaster.domain.Ticket;
import fi.haagahelia.taskmaster.taskmaster.dto.TicketDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/tickets")
public class TicketRestController {
    private final TicketRepository ticketRepository;
    private final BlockRepository blockRepository;

    @Autowired
    public TicketRestController(TicketRepository ticketRepository, BlockRepository blockRepository) {
        this.ticketRepository = ticketRepository;
        this.blockRepository = blockRepository;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ticketRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new Ticket
    @PostMapping
    public ResponseEntity<Ticket> newTicket(@RequestBody @NonNull TicketDTO ticketDTO) {
        Block block = blockRepository.findById(ticketDTO.getBlockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Block not found"));

        Integer maxSortOrder = ticketRepository
                .findMaxSortOrderByBlockId(ticketDTO.getBlockId())
                .orElse(-1);

        Ticket newTicket = new Ticket();
        newTicket.setTicketName(ticketDTO.getTicketName());
        newTicket.setDescription(ticketDTO.getDescription());
        newTicket.setBlock(block);
        newTicket.setSortOrder(maxSortOrder + 1);

        Ticket savedTicket = ticketRepository.save(newTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> editTicket(@PathVariable Long id,
            @RequestBody Ticket ticketData) {
        Ticket editTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ticket " + id + " can't be edited, since it doesn't exist."));

        // Update fields of editTicket with ticketData
        if (ticketData.getTicketName() != null) {
            editTicket.setTicketName(ticketData.getTicketName());
        }
        if (ticketData.getDescription() != null) {
            editTicket.setDescription(ticketData.getDescription());
        }
        if (ticketData.getStatus() != null) {
            editTicket.setStatus(ticketData.getStatus());
        }
        if (ticketData.getBlock() != null && ticketData.getBlock().getBlockId() != null) {
            Block newBlock = blockRepository.findById(ticketData.getBlock().getBlockId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Block not found"));
            editTicket.setBlock(newBlock);
        }

        ticketRepository.save(editTicket);

        return ResponseEntity.ok(editTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ticket " + id + " not found."));

        ticketRepository.delete(ticket);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reorder")
    public ResponseEntity<Void> reorderTickets(@RequestBody @NonNull List<TicketDTO> tickets,
            @RequestParam Long blockId) {
        
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Block not found"));

        for (TicketDTO ticketDTO : tickets) {
            Ticket ticket = ticketRepository.findById(ticketDTO.getTicketId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Ticket not found: " + ticketDTO.getTicketId()));

            if (!ticket.getBlock().getBlockId().equals(blockId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Ticket " + ticketDTO.getTicketId() + " does not belong to block " + blockId);
            }
        }
        int index = 0;
        for (TicketDTO ticketDTO : tickets) {
            Ticket ticket = ticketRepository.findById(ticketDTO.getTicketId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Ticket not found: " + ticketDTO.getTicketId()));

            ticket.setSortOrder(index++);
            ticketRepository.save(ticket);
        }

        return ResponseEntity.noContent().build();
    }
}