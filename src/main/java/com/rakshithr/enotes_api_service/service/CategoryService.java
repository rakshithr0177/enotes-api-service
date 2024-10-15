package com.rakshithr.enotes_api_service.service;

import com.rakshithr.enotes_api_service.dto.CategoryDto;
import com.rakshithr.enotes_api_service.dto.CategoryResponse;
import com.rakshithr.enotes_api_service.entity.Category;

import java.util.List;

public interface CategoryService {
    Boolean saveCategory(CategoryDto categoryDto);
    List<CategoryDto> getAllCategory();

    List<CategoryResponse> getActiveCategory();

    CategoryDto getCategoryById(Integer id) throws Exception;

    Boolean deleteCategory(Integer id);
}
