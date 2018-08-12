package mate_sebestyen.test.blogengine.api;

import mate_sebestyen.test.blogengine.data.*;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "/tags", produces = "application/json")
public class TagController {

    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TagController(TagRepository tagRepository, CategoryRepository categoryRepository) {
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
    }


    @GetMapping
    public Collection<Tag> getTags() {
        return tagRepository.findAll();
    }

    @PostMapping(consumes = "application/json")
    public Tag createTag(@RequestBody TagCreateOrUpdate newTag) {
        if (Strings.isBlank(newTag.name)) {
            throw new InvalidFieldException("name");
        }
        if (newTag.categoryId == null) {
            throw new InvalidFieldException("categoryId");
        }
        Category category = categoryRepository.findById(newTag.categoryId).orElseThrow(() -> new NotFoundException("Category", newTag.categoryId));
        Tag tag = tagRepository.save(new Tag(newTag.name, category));
        category.addTag(tag);
        categoryRepository.save(category);
        return tag;
    }

    @GetMapping("/{id}")
    public Tag getTag(@PathVariable Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new NotFoundException("Tag", id));
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public Tag updateTag(@PathVariable Long id, @RequestBody TagCreateOrUpdate tagUpdate) throws Tag.TagNameException {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new NotFoundException("Tag", id));
        if (!Strings.isBlank(tagUpdate.name)) {
            tag.setName(tagUpdate.name);
        }
        if (tagUpdate.categoryId != null) {
            categoryRepository.findById(tag.getCategory().getId()).ifPresent(category -> category.removeTag(tag));
            Category category = categoryRepository.findById(tagUpdate.categoryId).orElseThrow(() -> new NotFoundException("Category", tagUpdate.categoryId));
            tag.setCategory(category);
            category.addTag(tag);
        }
        return tagRepository.save(tag);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new NotFoundException("Tag", id));
        tagRepository.delete(tag);
    }
}
