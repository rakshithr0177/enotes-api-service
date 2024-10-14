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
import java.util.Optional;

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
        List<Category> categoryList = categoryRepo.findByIsDeletedFalse();
        List<CategoryDto> categoryDtoList = categoryList.stream().map(cat-> modelMapper.map(cat,CategoryDto.class)).toList();
        return categoryDtoList;
    }

    @Override
    public List<CategoryResponse> getActiveCategory() {
        List<Category> categoryList = categoryRepo.findByIsActiveTrueAndIsDeletedFalse();
        List<CategoryResponse> categoryResponseList = categoryList.stream().map(cat-> modelMapper.map(cat,CategoryResponse.class)).toList();
        return categoryResponseList;
    }

    @Override
    public CategoryDto getCategoryById(Integer id) {
        Optional<Category>findByCategory = categoryRepo.findByIdAndIsDeletedFalse(id);
        if(findByCategory.isPresent()){
            Category category = findByCategory.get();
            CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
            return categoryDto;
        }
        return null;
    }

    @Override
    public Boolean deleteCategory(Integer id) {
        Optional<Category>findByCategory = categoryRepo.findById(id);
        if(findByCategory.isPresent()){
            Category category = findByCategory.get();
            category.setIsDeleted(true);
            categoryRepo.save(category);
            return true;
        }
        return false;
    }
}
