package mate_sebestyen.test.blogengine.api;

import mate_sebestyen.test.blogengine.data.*;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "/categories", produces = "application/json")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public Collection<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping(consumes = "application/json")
    public Category createCategory(@RequestBody CategoryCreate newCategory) {
        if (Strings.isBlank(newCategory.name)) {
            throw new InvalidFieldException("name");
        }
        return categoryRepository.save(new Category(newCategory.name));
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category", id));
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public Category updateCategory(@PathVariable Long id, @RequestBody CategoryUpdate categoryUpdate) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category", id));
        if (!Strings.isBlank(categoryUpdate.name)) {
            category.setName(categoryUpdate.name);
        }
        if (categoryUpdate.newTagIds != null && !categoryUpdate.newTagIds.isEmpty()) {
            categoryUpdate.newTagIds.forEach(tagId -> tagRepository.findById(tagId).ifPresent(category::addTag));
        }
        if (categoryUpdate.deleteTagIds != null && !categoryUpdate.deleteTagIds.isEmpty()) {
            categoryUpdate.deleteTagIds.forEach(tagId -> tagRepository.findById(tagId).ifPresent(category::removeTag));
        }
        return categoryRepository.save(category);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category", id));
        categoryRepository.delete(category);
    }

    @GetMapping("/{id}/tags")
    public Collection<Tag> getTagsForCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category", id));
        return category.getTags();
    }
}
