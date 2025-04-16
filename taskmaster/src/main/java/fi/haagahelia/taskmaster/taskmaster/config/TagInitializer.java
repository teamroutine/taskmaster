package fi.haagahelia.taskmaster.taskmaster.config;

import fi.haagahelia.taskmaster.taskmaster.domain.Tag;
import fi.haagahelia.taskmaster.taskmaster.domain.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.*;
import java.util.List;

@Component
public class TagInitializer {

    private final TagRepository tagRepository;

    @Autowired
    public TagInitializer(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @PostConstruct
    public void initTags() {
        // Check if stock tags already exist
        if (tagRepository.count() == 0) {
            Tag tag1 = new Tag("Urgent", "#FF0000");
            Tag tag2 = new Tag("In Progress", "#FFFF00");
            Tag tag3 = new Tag("Completed", "#00FF00");
            Tag tag4 = new Tag("Bug", "#0000FF");
            tagRepository.saveAll(List.of(tag1, tag2, tag3, tag4));
        }
    }
}