package fi.haagahelia.taskmaster.taskmaster.dto;

public class TeamDto {
    private String teamName;
    private String description;

    public TeamDto() {
    }

    public TeamDto(String teamName, String description) {
        this.teamName = teamName;
        this.description = description;
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
}