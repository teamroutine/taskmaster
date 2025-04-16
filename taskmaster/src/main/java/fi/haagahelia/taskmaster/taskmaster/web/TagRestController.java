package fi.haagahelia.taskmaster.taskmaster.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.taskmaster.taskmaster.domain.Tag;
import fi.haagahelia.taskmaster.taskmaster.domain.TagRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.TagDto;
import io.micrometer.common.lang.NonNull;

@CrossOrigin
@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    private final TagRepository tagRepository;

    @Autowired
    public TagRestController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        return tagRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tag> createNewTag(@RequestBody @NonNull TagDto tagDto) {
        Tag newTag = new Tag();
        newTag.setName(tagDto.getName());
        newTag.setColor(tagDto.getColor());

        Tag savedTag = tagRepository.save(newTag);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTag);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> editTag(@PathVariable Long id, @RequestBody Tag tagData) {
        Tag editTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tag " + id + " can't be edited, since it doesn't exist."));

        if (tagData.getName() != null) {
            editTag.setName(tagData.getName());
        }
        if (tagData.getColor() != null) {
            editTag.setColor(tagData.getColor());
        }

        tagRepository.save(editTag);

        return ResponseEntity.ok(editTag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tag " + id + " not found."));

        tagRepository.delete(tag);
        return ResponseEntity.noContent().build();
    }

}
