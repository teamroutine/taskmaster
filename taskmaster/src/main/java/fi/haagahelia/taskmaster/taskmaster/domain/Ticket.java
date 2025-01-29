package fi.haagahelia.taskmaster.taskmaster.domain;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ticketId;
    private String ticketName;
    private String description;
    private Boolean status;
    @CreationTimestamp
    private LocalDate created;

    public Ticket() {
    }

    public Ticket(Long ticketId, String ticketName, String description, Boolean status, LocalDate created) {
        this.ticketId = ticketId;
        this.ticketName = ticketName;
        this.description = description;
        this.status = status;
        this.created = created;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Ticket [ticketId=" + ticketId + ", ticketName=" + ticketName + ", description=" + description
                + ", status=" + status + ", created=" + created + "]";
    }

}
