package fi.haagahelia.taskmaster.taskmaster.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.haagahelia.taskmaster.taskmaster.domain.Ticket;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDTO {
    private Long ticketId;
    private String ticketName;
    private String description;
    private Long blockId;

    public TicketDTO() {

    }

    public TicketDTO(Long ticketId, String ticketName, String description, Long blockId) {
        this.ticketId = ticketId;
        this.ticketName = ticketName;
        this.description = description;
        this.blockId = blockId;
    }

    public TicketDTO(Ticket ticket) {
        this.ticketId = ticket.getTicketId();
        this.ticketName = ticket.getTicketName();
        this.description = ticket.getDescription();
        this.blockId = ticket.getBlock() != null ? ticket.getBlock().getBlockId() : null;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    @Override
    public String toString() {
        return "TicketDTO [ticketId=" + ticketId + ", ticketName=" + ticketName + ", description=" + description
                + ", blockId=" + blockId + "]";
    }

}