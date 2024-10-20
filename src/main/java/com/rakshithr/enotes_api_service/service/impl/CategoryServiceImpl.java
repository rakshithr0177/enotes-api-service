package com.rakshithr.enotes_api_service.service.impl;

import com.rakshithr.enotes_api_service.dto.CategoryDto;
import com.rakshithr.enotes_api_service.dto.CategoryResponse;
import com.rakshithr.enotes_api_service.entity.Category;
import com.rakshithr.enotes_api_service.exception.ResourceNotFoundException;
import com.rakshithr.enotes_api_service.repository.CategoryRepository;
import com.rakshithr.enotes_api_service.service.CategoryService;
import com.rakshithr.enotes_api_service.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.lang.module.ResolutionException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepo;
    private final ModelMapper modelMapper;
    private final Validation validation;

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {

        //Validation Checking
        validation.categoryValidation(categoryDto);

        Category category = modelMapper.map(categoryDto, Category.class);

        if(ObjectUtils.isEmpty(category.getId())){
            category.setIsDeleted(false);
//            category.setCreatedBy(1);
//            category.setCreatedOn(new Date());
        }else{
            updateCategory(category);
        }
        Category savedCategory = categoryRepo.save(category);
        return !ObjectUtils.isEmpty(savedCategory);
    }

    private void updateCategory(Category category) {
        Optional<Category> findById = categoryRepo.findById(category.getId());
        if(findById.isPresent()){
            Category existCategory = findById.get();
            category.setCreatedBy(existCategory.getCreatedBy());
            category.setCreatedOn(existCategory.getCreatedOn());
            category.setIsDeleted(existCategory.getIsDeleted());

//            category.setUpdatedBy(1);
//            category.setUpdatedOn(new Date());

        }
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
    public CategoryDto getCategoryById(Integer id) throws Exception{
        Category category = categoryRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id = "+ id));
        if(!ObjectUtils.isEmpty(category)){
            return modelMapper.map(category, CategoryDto.class);
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
