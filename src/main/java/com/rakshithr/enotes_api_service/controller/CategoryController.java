package com.rakshithr.enotes_api_service.controller;

import com.rakshithr.enotes_api_service.dto.CategoryDto;
import com.rakshithr.enotes_api_service.dto.CategoryResponse;
import com.rakshithr.enotes_api_service.entity.Category;
import com.rakshithr.enotes_api_service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
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

    @GetMapping("/category")
    public ResponseEntity<?> getAllCategory(){
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        if(CollectionUtils.isEmpty(allCategory))
            return  ResponseEntity.noContent().build();
        else
            return  new ResponseEntity<>(allCategory, HttpStatus.OK);
    }

    @GetMapping("/active-category")
    public ResponseEntity<?> getActiveCategory(){
        List<CategoryResponse> allCategory = categoryService.getActiveCategory();
        if(CollectionUtils.isEmpty(allCategory))
            return  ResponseEntity.noContent().build();
        else
            return  new ResponseEntity<>(allCategory, HttpStatus.OK);
    }

}
