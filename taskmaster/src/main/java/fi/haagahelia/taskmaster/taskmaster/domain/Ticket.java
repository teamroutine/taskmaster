package fi.haagahelia.taskmaster.taskmaster.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTML.Tag;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GenerationType;

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

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "blockId")
    private Block block;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ticket_tags",
        joinColumns = @JoinColumn(name = "ticket_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonIgnoreProperties("tickets")
    private List<Tag> tags = new ArrayList<>();

    public Ticket() {
    }

    public Ticket(Long ticketId, String ticketName, String description, Boolean status, LocalDate created,
            Block block) {
        this.ticketId = ticketId;
        this.ticketName = ticketName;
        this.description = description;
        this.status = status;
        this.created = created;
        this.block = block;
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

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "Ticket [ticketId=" + ticketId + ", ticketName=" + ticketName + ", description=" + description
                + ", status=" + status + ", created=" + created + ", block=" + block + "]";
    }

}
