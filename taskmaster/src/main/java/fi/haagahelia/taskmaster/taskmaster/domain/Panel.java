package fi.haagahelia.taskmaster.taskmaster.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GenerationType;

@Entity
public class Panel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long panelId;
    private String panelName;
    private String description;
    
   
    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team team;

    public Panel() {
    }

    public Panel(Long panelId, String panelName, String description) {
        this.panelId = panelId;
        this.panelName = panelName;
        this.description = description;
    }

    public Long getPanelId() {
        return panelId;
    }

    public void setPanelId(Long panelId) {
        this.panelId = panelId;
    }

    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "Panel [panelId=" + panelId + ", panelName=" + panelName + ", description=" + description + ", team="
                + team + "]";
    }

    
}
