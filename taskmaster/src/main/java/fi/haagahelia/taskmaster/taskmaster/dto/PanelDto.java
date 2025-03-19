package fi.haagahelia.taskmaster.taskmaster.dto;

import fi.haagahelia.taskmaster.taskmaster.domain.Panel;

public class PanelDto {
    private Long panelId;
    private String panelName;
    private String description;
    private Long teamId;

    public PanelDto() {
    }

    public PanelDto(Long panelId, String panelName, String description, Long teamId) {
        this.panelId = panelId;
        this.panelName = panelName;
        this.description = description;
        this.teamId = teamId;
    }

    public PanelDto(Panel panel) {
        this.panelId = panel.getPanelId();
        this.panelName = panel.getPanelName();
        this.description = panel.getDescription();
        this.teamId = panel.getTeam().getTeamId();
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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
    @Override
    public String toString() {
        return "PanelDto [panelId=" + panelId + ", panelName=" + panelName + ", description=" + description
                + ", teamId=" + teamId + "]";
    }
}