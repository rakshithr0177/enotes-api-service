package com.rakshithr.enotes_api_service.controller;

import com.rakshithr.enotes_api_service.dto.CategoryDto;
import com.rakshithr.enotes_api_service.dto.CategoryResponse;
import com.rakshithr.enotes_api_service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/save-category")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto){
        Boolean saveCategory = categoryService.saveCategory(categoryDto);
        if(saveCategory)
            return  new ResponseEntity<>("saved success", HttpStatus.CREATED);
        else
            return  new ResponseEntity<>("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCategory(){
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        if(CollectionUtils.isEmpty(allCategory))
            return  ResponseEntity.noContent().build();
        else
            return  new ResponseEntity<>(allCategory, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveCategory(){
        List<CategoryResponse> allCategory = categoryService.getActiveCategory();
        if(CollectionUtils.isEmpty(allCategory))
            return  ResponseEntity.noContent().build();
        else
            return  new ResponseEntity<>(allCategory, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id){
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        if(ObjectUtils.isEmpty(categoryDto))
            return new ResponseEntity<>("Category not found with Id = "+ id, HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id){
        Boolean deleted = categoryService.deleteCategory(id);
        if(deleted)
            return new ResponseEntity<>("Category deleted success", HttpStatus.OK);
        else
            return new ResponseEntity<>("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
