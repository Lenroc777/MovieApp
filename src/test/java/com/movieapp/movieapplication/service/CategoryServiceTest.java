package com.movieapp.movieapplication.service;

import com.movieapp.movieapplication.model.Category;
import com.movieapp.movieapplication.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void testGetAllCategories() {
        // Given
        Category category1 = new Category("1", "Action", "Action movies");
        Category category2 = new Category("2", "Drama", "Drama movies");
        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        // When
        List<Category> categories = categoryService.getAllCategories();

        // Then
        assertEquals(2, categories.size());
        assertEquals("Action", categories.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryById_Found() {
        // Given
        String categoryId = "1";
        Category category = new Category(categoryId, "Action", "Action movies");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        Optional<Category> result = categoryService.getCategoryById(categoryId);

        // Then
        assertEquals(true, result.isPresent());
        assertEquals("Action", result.get().getName());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testGetCategoryById_NotFound() {
        // Given
        String categoryId = "1";
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        Optional<Category> result = categoryService.getCategoryById(categoryId);

        // Then
        assertEquals(false, result.isPresent());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testAddCategory() {
        // Given
        Category category = new Category("1", "Action", "Action movies");
        when(categoryRepository.save(category)).thenReturn(category);

        // When
        Category result = categoryService.addCategory(category);

        // Then
        assertEquals("Action", result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testDeleteCategory() {
        // Given
        String categoryId = "1";
        doNothing().when(categoryRepository).deleteById(categoryId);

        // When
        categoryService.deleteCategory(categoryId);

        // Then
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void testUpdateCategory_Found() {
        // Given
        String categoryId = "1";
        Category existingCategory = new Category(categoryId, "Action", "Action movies");
        Category updatedCategory = new Category(categoryId, "Drama", "Drama movies");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);

        // When
        Category result = categoryService.updateCategory(categoryId, updatedCategory);

        // Then
        assertEquals("Drama", result.getName());
        assertEquals("Drama movies", result.getDescription());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    void testUpdateCategory_NotFound() {
        // Given
        String categoryId = "1";
        Category updatedCategory = new Category(categoryId, "Drama", "Drama movies");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.updateCategory(categoryId, updatedCategory);
        });

        // Then
        assertEquals("Category not found with id: 1", exception.getMessage());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testGetAllCategories_EmptyList() {
        // Given
        when(categoryRepository.findAll()).thenReturn(List.of());

        // When
        List<Category> categories = categoryService.getAllCategories();

        // Then
        assertEquals(0, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }

}