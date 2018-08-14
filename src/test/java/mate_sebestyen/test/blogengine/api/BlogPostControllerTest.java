package mate_sebestyen.test.blogengine.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import mate_sebestyen.test.blogengine.api.data.BlogPostCreate;
import mate_sebestyen.test.blogengine.api.data.BlogPostUpdate;
import mate_sebestyen.test.blogengine.dal.BlogPostRepository;
import mate_sebestyen.test.blogengine.dal.TagRepository;
import mate_sebestyen.test.blogengine.model.BlogPost;
import mate_sebestyen.test.blogengine.model.Category;
import mate_sebestyen.test.blogengine.model.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogPostController.class)
public class BlogPostControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BlogPostRepository blogPostRepository;

    @MockBean
    private TagRepository tagRepository;

    @Test
    public void getBlogPosts_EmptyRepository() throws Exception {
        given(blogPostRepository.findAll()).willReturn(new ArrayList<>());

        mvc.perform(
                get("/blogposts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getBlogPosts_3Entries() throws Exception {

        List<BlogPost> blogPosts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            blogPosts.add(new BlogPost("BlogPost" + i, "content"));
        }

        given(blogPostRepository.findAll()).willReturn(blogPosts);

        ResultActions resultActions = mvc.perform(
                get("/blogposts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        for (int i = 0; i < 3; i++) {
            resultActions.andExpect(jsonPath("$[" + i + "].title", is("BlogPost" + i)));
            resultActions.andExpect(jsonPath("$[" + i + "].content", is("content")));
        }
    }

    @Test
    public void createBlogPost() throws Exception {
        BlogPost blogPost = new BlogPost("Blog Post", "content");
        BlogPostCreate blogPostCreate = new BlogPostCreate();
        blogPostCreate.title = "Blog Post";
        blogPostCreate.content = "content";

        when(blogPostRepository.save(ArgumentMatchers.any())).thenReturn(blogPost);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String content = mapper.writeValueAsString(blogPostCreate);
        ResultActions resultActions = mvc.perform(
                post("/blogposts")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        resultActions.andExpect(jsonPath("$.title", is("Blog Post")));
        resultActions.andExpect(jsonPath("$.content", is("content")));
    }

    @Test
    public void createBlogPostNoTitle() throws Exception {
        BlogPost blogPost = new BlogPost("Blog Post", "content");
        BlogPostCreate blogPostCreate = new BlogPostCreate();
        blogPostCreate.content = "content";

        when(blogPostRepository.save(ArgumentMatchers.any())).thenReturn(blogPost);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String content = mapper.writeValueAsString(blogPostCreate);
        ResultActions resultActions = mvc.perform(
                post("/blogposts")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        assertTrue(InvalidFieldException.class.isInstance(resultActions.andReturn().getResolvedException()));
    }

    @Test
    public void getNotExisting() throws Exception {
        when(blogPostRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        ResultActions resultActions = mvc.perform(
                get("/blogposts/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        assertTrue(NotFoundException.class.isInstance(resultActions.andReturn().getResolvedException()));
    }

    @Test
    public void getExisting() throws Exception {
        BlogPost blogPost = new BlogPost("Blogpost", "content");
        when(blogPostRepository.findById((long) 1)).thenReturn(Optional.of(blogPost));
        ResultActions resultActions = mvc.perform(
                get("/blogposts/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        resultActions.andExpect(jsonPath("$.title", is("Blogpost")));
        resultActions.andExpect(jsonPath("$.content", is("content")));
    }

    @Test
    public void putNotExisting() throws Exception {
        BlogPostUpdate update = new BlogPostUpdate();
        update.title = "new Title";
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String content = mapper.writeValueAsString(update);

        when(blogPostRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        ResultActions resultActions = mvc.perform(
                put("/blogposts/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content)
        );
        assertTrue(NotFoundException.class.isInstance(resultActions.andReturn().getResolvedException()));
    }

    @Test
    public void putExisting() throws Exception {
        BlogPostUpdate update = new BlogPostUpdate();
        update.title = "new Title";
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String content = mapper.writeValueAsString(update);

        BlogPost blogPost = new BlogPost("Blogpost", "content");
        when(blogPostRepository.findById((long) 1)).thenReturn(Optional.of(blogPost));
        when(blogPostRepository.save(blogPost)).thenReturn(blogPost);

        ResultActions resultActions = mvc.perform(
                put("/blogposts/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content)

        );
        resultActions.andExpect(jsonPath("$.title", is("new Title")));
        resultActions.andExpect(jsonPath("$.content", is("content")));
    }

    @Test
    public void deleteNotExisting() throws Exception {
        when(blogPostRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        ResultActions resultActions = mvc.perform(
                delete("/blogposts/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        assertTrue(NotFoundException.class.isInstance(resultActions.andReturn().getResolvedException()));
    }

    @Test
    public void getTags() throws Exception {
        BlogPost blogPost = new BlogPost("Blogpost", "content");
        when(blogPostRepository.findById((long) 1)).thenReturn(Optional.of(blogPost));

        ResultActions resultActions = mvc.perform(
                get("/blogposts/1/tags")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getByNotExistingTag() throws Exception {
        when(tagRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        ResultActions resultActions = mvc.perform(
                get("/blogposts/bytag/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        assertTrue(NotFoundException.class.isInstance(resultActions.andReturn().getResolvedException()));
    }

    @Test
    public void getByExistingTag() throws Exception {
        Tag tag = new Tag("Tag", new Category("Category"));
        when(tagRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(tag));
        when(blogPostRepository.findAllByTagsContaining(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Page.empty());
        ResultActions resultActions = mvc.perform(
                get("/blogposts/bytag/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content", hasSize(0)));
    }
}
