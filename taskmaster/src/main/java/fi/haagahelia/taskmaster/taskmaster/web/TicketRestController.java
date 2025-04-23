package fi.haagahelia.taskmaster.taskmaster.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.taskmaster.taskmaster.domain.Block;
import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Tag;
import fi.haagahelia.taskmaster.taskmaster.domain.TagRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Ticket;
import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.TicketDTO;

@CrossOrigin
@RestController
@RequestMapping("/api/tickets")
public class TicketRestController {
    private final TicketRepository ticketRepository;
    private final BlockRepository blockRepository;
    private final TagRepository tagRepository;

    @Autowired
    public TicketRestController(TicketRepository ticketRepository, BlockRepository blockRepository,
            TagRepository tagRepository) {
        this.ticketRepository = ticketRepository;
        this.blockRepository = blockRepository;
        this.tagRepository = tagRepository;
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

        Ticket newTicket = new Ticket();
        newTicket.setTicketName(ticketDTO.getTicketName());
        newTicket.setDescription(ticketDTO.getDescription());
        newTicket.setDueDate(ticketDTO.getDueDate());
        newTicket.setBlock(block);

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
        if (ticketData.getDueDate() != null) {
            editTicket.setDueDate(ticketData.getDueDate());
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

    @PutMapping("/{id}/addTags")
    public ResponseEntity<Ticket> addTagsToTickets(@PathVariable Long id, @RequestBody List<Long> tagIds) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        List<Tag> tags = tagRepository.findAllById(tagIds);
        ticket.getTags().addAll(tags);

        ticketRepository.save(ticket);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{id}/removeTags")
    public ResponseEntity<Ticket> removeTagsFromTickets(@PathVariable Long id, @RequestBody List<Long> tagIds) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        List<Tag> tags = tagRepository.findAllById(tagIds);
        ticket.getTags().removeAll(tags);

        ticketRepository.save(ticket);
        return ResponseEntity.ok(ticket);
    }

}