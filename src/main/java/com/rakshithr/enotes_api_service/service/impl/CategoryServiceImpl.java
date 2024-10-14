package com.rakshithr.enotes_api_service.service.impl;

import com.rakshithr.enotes_api_service.dto.CategoryDto;
import com.rakshithr.enotes_api_service.dto.CategoryResponse;
import com.rakshithr.enotes_api_service.entity.Category;
import com.rakshithr.enotes_api_service.repository.CategoryRepository;
import com.rakshithr.enotes_api_service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepo;
    private final ModelMapper modelMapper;
    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {
//        Category category = new Category();
//        category.setName(categoryDto.getName());
//        category.setDescription(categoryDto.getDescription());
//        category.setIsActive(categoryDto.getIsActive());

        Category category = modelMapper.map(categoryDto, Category.class);

        category.setIsDeleted(false);
        category.setCreatedBy(1);
        category.setCreatedOn(new Date());
        Category savedCategory = categoryRepo.save(category);
        return !ObjectUtils.isEmpty(savedCategory);
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categoryList = categoryRepo.findAll();
        List<CategoryDto> categoryDtoList = categoryList.stream().map(cat-> modelMapper.map(cat,CategoryDto.class)).toList();
        return categoryDtoList;
    }

    @Override
    public List<CategoryResponse> getActiveCategory() {
        List<Category> categoryList = categoryRepo.findByIsActiveTrue();
        List<CategoryResponse> categoryResponseList = categoryList.stream().map(cat-> modelMapper.map(cat,CategoryResponse.class)).toList();
        return categoryResponseList;
    }
}
