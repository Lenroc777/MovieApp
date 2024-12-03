package com.movieapp.movieapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapp.movieapplication.model.Category;
import com.movieapp.movieapplication.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        CategoryController categoryController = new CategoryController(categoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllCategories() throws Exception {
        // Given
        Category category1 = new Category("1", "Action", "Action movies");
        Category category2 = new Category("2", "Drama", "Drama movies");

        when(categoryService.getAllCategories()).thenReturn(List.of(category1, category2));

        // When & Then
        mockMvc.perform(get("/api/categories/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Action"))
                .andExpect(jsonPath("$[1].name").value("Drama"));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void testGetCategoryById_Found() throws Exception {
        // Given
        String categoryId = "1";
        Category category = new Category(categoryId, "Action", "Action movies");

        when(categoryService.getCategoryById(categoryId)).thenReturn(Optional.of(category));

        // When & Then
        mockMvc.perform(get("/api/categories/" + categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Action"))
                .andExpect(jsonPath("$.description").value("Action movies"));

        verify(categoryService, times(1)).getCategoryById(categoryId);
    }

    @Test
    void testGetCategoryById_NotFound() throws Exception {
        // Given
        String categoryId = "1";

        when(categoryService.getCategoryById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/categories/" + categoryId))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).getCategoryById(categoryId);
    }

    @Test
    void testAddCategory() throws Exception {
        // Given
        Category category = new Category("1", "Action", "Action movies");

        when(categoryService.addCategory(any(Category.class))).thenReturn(category);

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Action"))
                .andExpect(jsonPath("$.description").value("Action movies"));

        verify(categoryService, times(1)).addCategory(any(Category.class));
    }

    @Test
    void testUpdateCategory_Found() throws Exception {
        // Given
        String categoryId = "1";
        Category updatedCategory = new Category(categoryId, "Drama", "Drama movies");

        when(categoryService.updateCategory(eq(categoryId), any(Category.class))).thenReturn(updatedCategory);

        // When & Then
        mockMvc.perform(put("/api/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Drama"))
                .andExpect(jsonPath("$.description").value("Drama movies"));

        verify(categoryService, times(1)).updateCategory(eq(categoryId), any(Category.class));
    }


    @Test
    void testDeleteCategory() throws Exception {
        // Given
        String categoryId = "1";

        doNothing().when(categoryService).deleteCategory(categoryId);

        // When & Then
        mockMvc.perform(delete("/api/categories/" + categoryId))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(categoryId);
    }
}