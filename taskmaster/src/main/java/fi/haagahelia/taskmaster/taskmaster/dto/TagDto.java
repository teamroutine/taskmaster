package fi.haagahelia.taskmaster.taskmaster.dto;

public class TagDto {

    private String name;
    private String color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TagDto(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public TagDto() {
    }
}