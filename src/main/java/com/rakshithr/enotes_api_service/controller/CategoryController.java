package com.rakshithr.enotes_api_service.controller;

import com.rakshithr.enotes_api_service.dto.CategoryDto;
import com.rakshithr.enotes_api_service.dto.CategoryResponse;
import com.rakshithr.enotes_api_service.exception.ResourceNotFoundException;
import com.rakshithr.enotes_api_service.service.CategoryService;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/save")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto){
        Boolean saveCategory = categoryService.saveCategory(categoryDto);
        if(saveCategory)
            return CommonUtil.createBuildResponseMessage("saved success", HttpStatus.CREATED);
//            return  new ResponseEntity<>("saved success", HttpStatus.CREATED);
        else
            return CommonUtil.createErrorResponseMessage("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
//            return  new ResponseEntity<>("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCategory(){
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        if(CollectionUtils.isEmpty(allCategory))
            return  ResponseEntity.noContent().build();
        else
            return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
//            return  new ResponseEntity<>(allCategory, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveCategory(){
        List<CategoryResponse> allCategory = categoryService.getActiveCategory();
        if(CollectionUtils.isEmpty(allCategory))
            return  ResponseEntity.noContent().build();
        else
            return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
//            return  new ResponseEntity<>(allCategory, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception {
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        if(ObjectUtils.isEmpty(categoryDto))
            return CommonUtil.createErrorResponseMessage("Internal Server Error", HttpStatus.NOT_FOUND);
//            return new ResponseEntity<>("Internal Server Error", HttpStatus.NOT_FOUND);

        return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
//        return new ResponseEntity<>(categoryDto, HttpStatus.OK);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id){
        Boolean deleted = categoryService.deleteCategory(id);
        if(deleted)
            return CommonUtil.createBuildResponseMessage("Category deleted success", HttpStatus.OK);
//        return new ResponseEntity<>("Category deleted success", HttpStatus.OK);
        else
            return CommonUtil.createErrorResponseMessage("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
//        return new ResponseEntity<>("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
