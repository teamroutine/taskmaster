package fi.haagahelia.taskmaster.taskmaster.domain;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long inviteId;
    private String nanoId;
    private Date createdAt;
    private Date expiresAt;

    @ManyToOne
    private Team team;

    public Invite() {
    }

    public Invite(String nanoId, Date createdAt, Date expiresAt, Team team) {
        this.nanoId = nanoId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.team = team;
    }

    public Long getInviteId() {
        return inviteId;
    }

    public void setInviteId(Long inviteId) {
        this.inviteId = inviteId;
    }

    public String getNanoId() {
        return nanoId;
    }

    public void setNanoId(String nanoId) {
        this.nanoId = nanoId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "Invite [inviteId=" + inviteId + ", nanoId=" + nanoId + ", createdAt=" + createdAt + ", expiresAt=" + expiresAt + ", team=" + team + "]";
    }
}