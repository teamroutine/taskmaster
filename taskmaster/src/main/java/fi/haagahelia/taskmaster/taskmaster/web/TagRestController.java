package fi.haagahelia.taskmaster.taskmaster.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/api/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tags", description = "Endpoints for managing tags")
public class TagRestController {

    private final TagRepository tagRepository;

    @Autowired
    public TagRestController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Operation(summary = "Get all tags", description = "Return a list of all tags")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tags")
    })
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return ResponseEntity.ok(tags);
    }

    @Operation(summary = "Get a tag by ID", description = "Returns a single tag by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag found"),
        @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        return tagRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new tag", description = "Creates and returns a new tag")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tag successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Tag> createNewTag(@RequestBody @NonNull TagDto tagDto) {
        Tag newTag = new Tag();
        newTag.setName(tagDto.getName());
        newTag.setColor(tagDto.getColor());

        Tag savedTag = tagRepository.save(newTag);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTag);
    }

    @Operation(summary = "Edit a tag", description = "Updates an existing tag by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag successfully updated"),
        @ApiResponse(responseCode = "404", description = "Tag not found")
    })
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

    @Operation(summary = "Delete a tag", description = "Deletes a tag by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tag successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tag " + id + " not found."));

        tagRepository.delete(tag);
        return ResponseEntity.noContent().build();
    }

}
