package fi.haagahelia.taskmaster.taskmaster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GenerationType;

@Entity
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long blockId;
    private String blockName;
    private String description;
    private String highlightColor;

    @ManyToOne
    @JsonIgnoreProperties("panels")
    @JoinColumn(name = "panelId")
    private Panel panel;

    public Block() {
    }

    public Block(Long blockId, String blockName, String description, String highlightColor) {
        this.blockId = blockId;
        this.blockName = blockName;
        this.description = description;
        this.highlightColor = highlightColor;
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
    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    @Override
    public String toString() {
        return "Block [blockId=" + blockId + ", blockName=" + blockName + ", description=" + description
                + ", highlightColor=" + highlightColor + ", panel=" + panel + "]";
    }

    

}
