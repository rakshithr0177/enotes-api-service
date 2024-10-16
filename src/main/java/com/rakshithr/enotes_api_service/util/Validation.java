package com.rakshithr.enotes_api_service.util;

import com.rakshithr.enotes_api_service.dto.CategoryDto;
import com.rakshithr.enotes_api_service.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Validation {

    public void categoryValidation(CategoryDto categoryDto){

        Map<String, Object> error = new LinkedHashMap<>();

        if(ObjectUtils.isEmpty(categoryDto)){
            throw new IllegalArgumentException("Category Object/JSON shouldn't be null or empty");
        } else{
            //validation name field
            if(ObjectUtils.isEmpty(categoryDto.getName())){
                error.put("name","name field is empty or null");
            }else {
                if (categoryDto.getName().length() < 10){
                    error.put("name", "name length min 10");
                }
                if (categoryDto.getName().length() > 100){
                    error.put("name", "name length max 100");
                }
            }

            //validation description field
            if(ObjectUtils.isEmpty(categoryDto.getDescription())){
                error.put("description","description field is empty or null");
            }

            //validation isActive field
            if(ObjectUtils.isEmpty(categoryDto.getIsActive())){
                error.put("isActive","isActive field is empty or null");
            }else{
                if(categoryDto.getIsActive() != Boolean.TRUE.booleanValue() && categoryDto.getIsActive() != Boolean.FALSE.booleanValue()){
                    error.put("isActive", "invalid value isActive field");
                }
            }
        }
        if(!error.isEmpty()){
            throw new ValidationException(error);
        }
    }
}
