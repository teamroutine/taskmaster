package fi.haagahelia.taskmaster.taskmaster.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long teamId;
    private String teamName;
    private String description;
    private String createdBy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    private List<Panel> panels;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(name = "team_appuser", joinColumns = { @JoinColumn(name = "teamId") }, inverseJoinColumns = {
            @JoinColumn(name = "id") })
    @JsonIgnoreProperties("teams")
    List<AppUser> appusers;

    public Team() {
    }

    public Team(Long teamId, String teamName, String description, List<Panel> panels, List<AppUser> appusers, String createdBy) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.description = description;
        this.panels = panels;
        this.appusers = appusers;
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Panel> getPanels() {
        return panels;
    }

    public void setPanels(List<Panel> panels) {
        this.panels = panels;
    }

    public List<AppUser> getAppUsers() {
        return appusers;
    }

    public void setAppUsers(List<AppUser> appusers) {
        this.appusers = appusers;
    }

    @Override
    public String toString() {
        return "Team [teamId=" + teamId + ", teamName=" + teamName + ", description=" + description + ", panels="
                + panels + ", appusers=" + appusers + "]";
    }

}
