package mate_sebestyen.test.blogengine.api;

import mate_sebestyen.test.blogengine.api.data.BlogPostCreate;
import mate_sebestyen.test.blogengine.api.data.BlogPostUpdate;
import mate_sebestyen.test.blogengine.dal.BlogPostRepository;
import mate_sebestyen.test.blogengine.dal.TagRepository;
import mate_sebestyen.test.blogengine.model.BlogPost;
import mate_sebestyen.test.blogengine.model.Tag;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/blogposts")
public class BlogPostController {

    private final BlogPostRepository blogPostRepository;
    private final TagRepository tagRepository;

    @Autowired
    public BlogPostController(BlogPostRepository blogPostRepository, TagRepository tagRepository) {
        this.blogPostRepository = blogPostRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public Collection<BlogPost> getBlogPosts() {
        return blogPostRepository.findAll();
    }

    @PostMapping
    public BlogPost createBlogPost(@RequestBody BlogPostCreate newBlogPost) {
        if (Strings.isBlank(newBlogPost.title)) {
            throw new InvalidFieldException("title");
        }
        if (Strings.isBlank(newBlogPost.content)) {
            throw new InvalidFieldException("content");
        }
        BlogPost blogPost = new BlogPost(newBlogPost.title, newBlogPost.content);

        if (newBlogPost.tagIds != null && !newBlogPost.tagIds.isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            newBlogPost.tagIds.forEach(tagId -> tagRepository.findById(tagId).ifPresent(tags::add));
            blogPost.setTags(tags);
        }
        return blogPostRepository.save(blogPost);
    }

    @GetMapping("/{id}")
    public BlogPost getBlogPost(@PathVariable Long id) {
        return blogPostRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog post", id));
    }

    @PutMapping("/{id}")
    public BlogPost updateBlogPost(@PathVariable Long id, @RequestBody BlogPostUpdate blogPostUpdate) {
        BlogPost blogPost = blogPostRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog post", id));
        if (blogPostUpdate.title != null) {
            blogPost.setTitle(blogPostUpdate.title);
        }
        if (blogPostUpdate.content != null) {
            blogPost.setContent(blogPostUpdate.content);
        }
        if (blogPostUpdate.newTagIds != null && !blogPostUpdate.newTagIds.isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            blogPostUpdate.newTagIds.forEach(tagId -> tagRepository.findById(tagId).ifPresent(tags::add));
            blogPost.getTags().addAll(tags);
        }
        if (blogPostUpdate.deleteTagIds != null && !blogPostUpdate.deleteTagIds.isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            blogPostUpdate.deleteTagIds.forEach(tagId -> tagRepository.findById(tagId).ifPresent(tags::add));
            blogPost.getTags().removeAll(tags);
        }

        return blogPostRepository.save(blogPost);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBlogPost(@PathVariable Long id) {
        BlogPost blogPost = blogPostRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog post", id));
        blogPostRepository.delete(blogPost);
    }

    @GetMapping("/{id}/tags")
    public Collection<Tag> getTagsForBlogPost(@PathVariable("id") long id) {
        BlogPost blogPost = blogPostRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog post", id));
        return blogPost.getTags();
    }

    @GetMapping("/bytag/{tagId}")
    public Collection<BlogPost> getBlogPostsByTag(@PathVariable("tagId") long tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new NotFoundException("Tag", tagId));
        return blogPostRepository.findByTagsIsContaining(tag);
    }
}
