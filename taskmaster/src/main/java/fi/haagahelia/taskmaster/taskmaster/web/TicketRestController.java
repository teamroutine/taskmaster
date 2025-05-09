package fi.haagahelia.taskmaster.taskmaster.web;

import java.util.List;

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
import fi.haagahelia.taskmaster.taskmaster.dto.TicketDto;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/api/tickets")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tickets", description = "Endpoints for managing tickets")
public class TicketRestController {
        private final TicketRepository ticketRepository;
        private final BlockRepository blockRepository;
        private final TagRepository tagRepository;

        public TicketRestController(TicketRepository ticketRepository, BlockRepository blockRepository,
                        TagRepository tagRepository) {
                this.ticketRepository = ticketRepository;
                this.blockRepository = blockRepository;
                this.tagRepository = tagRepository;
        }

        @Operation(summary = "Get all tickets", description = "Return a list of all tickets")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Tickets successfully retrieved")
        })
        @GetMapping
        public ResponseEntity<List<Ticket>> getAllTickets() {
                List<Ticket> tickets = ticketRepository.findAll();
                return ResponseEntity.ok(tickets);
        }

        @Operation(summary = "Get a ticket by ID", description = "Return a single ticket by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Ticket found"),
                        @ApiResponse(responseCode = "404", description = "Ticket not found")
        })
        @GetMapping("/{id}")
        public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
                return ticketRepository.findById(id)
                                .map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @Operation(summary = "Create a new ticket", description = "Creates and returns a new ticket")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Ticket successfully created"),
                        @ApiResponse(responseCode = "404", description = "Block not found")
        })
        @PostMapping
        public ResponseEntity<Ticket> newTicket(@RequestBody @NonNull TicketDto ticketDTO) {
                Block block = blockRepository.findById(ticketDTO.getBlockId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Block not found"));

                Integer maxSortOrder = ticketRepository
                                .findMaxSortOrderByBlockId(ticketDTO.getBlockId())
                                .orElse(-1);

                Ticket newTicket = new Ticket();
                newTicket.setTicketName(ticketDTO.getTicketName());
                newTicket.setDescription(ticketDTO.getDescription());
                newTicket.setDueDate(ticketDTO.getDueDate());
                newTicket.setBlock(block);
                newTicket.setSortOrder(maxSortOrder + 1);

                Ticket savedTicket = ticketRepository.save(newTicket);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
        }

        @Operation(summary = "Edit existing ticket", description = "Update ticket fields by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Ticket successfully updated"),
                        @ApiResponse(responseCode = "404", description = "Ticket or Block not found")
        })
        @PutMapping("/{id}")
        public ResponseEntity<Ticket> editTicket(@PathVariable Long id,
                        @RequestBody Ticket ticketData) {
                Ticket editTicket = ticketRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Ticket " + id + " can't be edited, since it doesn't exist."));
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

        @Operation(summary = "Delete a ticket", description = "Delete a ticket by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Ticket successfully deleted"),
                        @ApiResponse(responseCode = "404", description = "Ticket not found")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
                Ticket ticket = ticketRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Ticket " + id + " not found."));

                ticketRepository.delete(ticket);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Add tags to a ticket", description = "Attach one or more tags to an existing ticket")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Tags successfully added"),
                        @ApiResponse(responseCode = "404", description = "Ticket not found")
        })
        @PutMapping("/{id}/addTags")
        public ResponseEntity<Ticket> addTagsToTickets(@PathVariable Long id, @RequestBody List<Long> tagIds) {
                Ticket ticket = ticketRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Ticket not found"));

                List<Tag> tags = tagRepository.findAllById(tagIds);
                ticket.getTags().addAll(tags);

                ticketRepository.save(ticket);
                return ResponseEntity.ok(ticket);
        }

        @Operation(summary = "Remove tags from a ticket", description = "Detach one or more tags from an existing ticket")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Tags successfully removed"),
                        @ApiResponse(responseCode = "404", description = "Ticket not found")
        })
        @PutMapping("/{id}/removeTags")
        public ResponseEntity<Ticket> removeTagsFromTickets(@PathVariable Long id, @RequestBody List<Long> tagIds) {
                Ticket ticket = ticketRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Ticket not found"));

                List<Tag> tags = tagRepository.findAllById(tagIds);
                ticket.getTags().removeAll(tags);

                ticketRepository.save(ticket);
                return ResponseEntity.ok(ticket);
        }

        @Operation(summary = "Reorder tickets in a block", description = "Change the sort order of tickets within a block")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Tickets successfully reordered"),
                        @ApiResponse(responseCode = "400", description = "Ticket does not belong to the specified block"),
                        @ApiResponse(responseCode = "404", description = "Ticket or Block not found")
        })
        @PutMapping("/reorder")
        public ResponseEntity<Void> reorderTickets(@RequestBody @NonNull List<TicketDto> tickets,
                        @RequestParam Long blockId) {

                Block block = blockRepository.findById(blockId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Block not found"));

                for (TicketDto ticketDTO : tickets) {
                        Ticket ticket = ticketRepository.findById(ticketDTO.getTicketId())
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                        "Ticket not found: " + ticketDTO.getTicketId()));

                        if (!ticket.getBlock().getBlockId().equals(blockId)) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "Ticket " + ticketDTO.getTicketId() + " does not belong to block "
                                                                + blockId);
                        }
                }
                int index = 0;
                for (TicketDto ticketDTO : tickets) {
                        Ticket ticket = ticketRepository.findById(ticketDTO.getTicketId())
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                        "Ticket not found: " + ticketDTO.getTicketId()));

                        ticket.setSortOrder(index++);
                        ticketRepository.save(ticket);
                }

                return ResponseEntity.noContent().build();
        }
}