package mate_sebestyen.test.blogengine.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import mate_sebestyen.test.blogengine.api.data.CategoryCreate;
import mate_sebestyen.test.blogengine.api.data.CategoryUpdate;
import mate_sebestyen.test.blogengine.dal.CategoryRepository;
import mate_sebestyen.test.blogengine.dal.TagRepository;
import mate_sebestyen.test.blogengine.model.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private TagRepository tagRepository;


    @Test
    public void getCategory_EmptyRepository() throws Exception {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        mvc.perform(
                get("/categories")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getCategory_3Entries() throws Exception {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            categories.add(new Category("Category" + i));
        }
        when(categoryRepository.findAll()).thenReturn(categories);

        ResultActions resultActions = mvc.perform(
                get("/categories")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        for (int i = 0; i < 3; i++) {
            resultActions.andExpect(jsonPath("$[" + i + "].name", is("Category" + i)));

        }
    }

    @Test
    public void createCategory() throws Exception {
        Category category = new Category("Category");
        CategoryCreate categoryCreate = new CategoryCreate();
        categoryCreate.name = "Category";

        when(categoryRepository.save(ArgumentMatchers.any())).thenReturn(category);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String content = mapper.writeValueAsString(categoryCreate);
        ResultActions resultActions = mvc.perform(
                post("/categories")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        resultActions.andExpect(jsonPath("$.name", is("Category")));
    }

    @Test
    public void getNotExisting() throws Exception {
        when(categoryRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        ResultActions resultActions = mvc.perform(
                get("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        assertTrue(NotFoundException.class.isInstance(resultActions.andReturn().getResolvedException()));
    }

    @Test
    public void getExisting() throws Exception {
        Category category = new Category("Category");
        when(categoryRepository.findById((long) 1)).thenReturn(Optional.of(category));
        ResultActions resultActions = mvc.perform(
                get("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        resultActions.andExpect(jsonPath("$.name", is("Category")));
    }

    @Test
    public void putNotExisting() throws Exception {
        when(categoryRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        CategoryUpdate update = new CategoryUpdate();
        update.name = "new_Title";
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String content = mapper.writeValueAsString(update);

        ResultActions resultActions = mvc.perform(
                put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content)
        );
        assertTrue(NotFoundException.class.isInstance(resultActions.andReturn().getResolvedException()));
    }

    @Test
    public void putExisting() throws Exception {
        Category category = new Category("Category");
        when(categoryRepository.findById((long) 1)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryUpdate update = new CategoryUpdate();
        update.name = "new Name";
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String content = mapper.writeValueAsString(update);

        ResultActions resultActions = mvc.perform(
                put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content)

        );
        resultActions.andExpect(jsonPath("$.name", is("new Name")));
    }

    @Test
    public void deleteNotExisting() throws Exception {
        when(categoryRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        ResultActions resultActions = mvc.perform(
                delete("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        assertTrue(NotFoundException.class.isInstance(resultActions.andReturn().getResolvedException()));
    }

    @Test
    public void getTags() throws Exception {
        Category category = new Category("Category");
        when(categoryRepository.findById((long) 1)).thenReturn(Optional.of(category));

        ResultActions resultActions = mvc.perform(
                get("/categories/1/tags")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(0)));
    }
}
