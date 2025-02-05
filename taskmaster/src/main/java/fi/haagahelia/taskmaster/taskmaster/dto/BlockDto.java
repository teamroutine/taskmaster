package fi.haagahelia.taskmaster.taskmaster.dto;

public class BlockDto {

    private Long blockId;
    private String blockName;
    private String description;
    private String highlightColor;
    private Long panelId;

    public BlockDto(){}

    public BlockDto(Long blockId, String blockName, String description, String highlightColor, Long panelId) {
        this.blockId = blockId;
        this.blockName = blockName;
        this.description = description;
        this.highlightColor = highlightColor;
        this.panelId = panelId;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(String highlightColor) {
        this.highlightColor = highlightColor;
    }

    public Long getPanelId() {
        return panelId;
    }

    public void setPanelId(Long panelId) {
        this.panelId = panelId;
    }

    
}
