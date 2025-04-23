package fi.haagahelia.taskmaster.taskmaster.dto;

import java.time.LocalDate;

import fi.haagahelia.taskmaster.taskmaster.domain.Ticket;

public class TicketDTO {
    private Long ticketId;
    private String ticketName;
    private String description;
    private LocalDate dueDate;
    private Long blockId;

    public TicketDTO() {

    }

    public TicketDTO(Long ticketId, String ticketName, String description, LocalDate dueDate, Long blockId) {
        this.ticketId = ticketId;
        this.ticketName = ticketName;
        this.description = description;
        this.dueDate = dueDate;
        this.blockId = blockId;
    }

    public TicketDTO(Ticket ticket) {
        this.ticketId = ticket.getTicketId();
        this.ticketName = ticket.getTicketName();
        this.description = ticket.getDescription();
        this.dueDate = ticket.getDueDate();
        this.blockId = ticket.getBlock().getBlockId();
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
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
                + ", dueDate=" + dueDate + ", blockId=" + blockId + "]";
    }

   
}