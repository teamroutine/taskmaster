package fi.haagahelia.taskmaster.taskmaster.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long teamId;
    private String teamName;
    private String description;

   
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    private List <Panel> panels;

    public Team() {
    }

    

    public Team(Long teamId, String teamName, String description, List<Panel> panels) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.description = description;
        this.panels = panels;
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

    @Override
    public String toString() {
        return "Team [teamId=" + teamId + ", teamName=" + teamName + ", description=" + description + ", panels="
                + panels + "]";
    }
    

}
