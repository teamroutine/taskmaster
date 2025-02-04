package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Panel;
import fi.haagahelia.taskmaster.taskmaster.domain.Ticket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@CrossOrigin
@RestController
@RequestMapping("/api/tickets")
public class TicketRestController {
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketRestController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return ResponseEntity.ok(tickets);
    }

    // Create a new Ticket
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED, reason = "New ticket created")
    public Ticket newTicket(@RequestBody @NonNull Ticket newTicket) {
        return ticketRepository.save(newTicket);
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

}