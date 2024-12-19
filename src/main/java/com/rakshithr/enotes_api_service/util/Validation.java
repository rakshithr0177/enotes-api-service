package com.rakshithr.enotes_api_service.util;

import com.rakshithr.enotes_api_service.dto.CategoryDto;
import com.rakshithr.enotes_api_service.dto.TodoDto;
import com.rakshithr.enotes_api_service.dto.UserDto;
import com.rakshithr.enotes_api_service.entity.Role;
import com.rakshithr.enotes_api_service.enums.TodoStatus;
import com.rakshithr.enotes_api_service.exception.ResourceNotFoundException;
import com.rakshithr.enotes_api_service.exception.ValidationException;
import com.rakshithr.enotes_api_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Validation {

    private final RoleRepository roleRepository;

    public void categoryValidation(CategoryDto categoryDto){

        Map<String, Object> error = new LinkedHashMap<>();

        if(ObjectUtils.isEmpty(categoryDto)){
            throw new IllegalArgumentException("Category Object/JSON shouldn't be null or empty");
        } else{
            //validation name field
            if(ObjectUtils.isEmpty(categoryDto.getName())){
                error.put("name","name field is empty or null");
            }else {
                if (categoryDto.getName().length() < 3){
                    error.put("name", "name length min 3");
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

    public void todoValidation(TodoDto todo) throws Exception{
        TodoDto.StatusDto reqStatus = todo.getStatus();
        TodoStatus[] status = TodoStatus.values();

        boolean statusFound = false;

        for(TodoStatus st : status){
            if(st.getId().equals(reqStatus.getId()) ){
                statusFound = true;
            }
        }

        if (!statusFound){
            throw new ResourceNotFoundException("invalid status");
        }

    }

    public void userValidation(UserDto userDto){

        if(!StringUtils.hasText(userDto.getFirstName())){
            throw new IllegalArgumentException("first name is invalid");
        }

        if(!StringUtils.hasText(userDto.getLastName())){
            throw new IllegalArgumentException("last name is invalid");
        }

        if(!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constants.EMAIL_REGEX)){
            throw new IllegalArgumentException("email is invalid");
        }

        if(!StringUtils.hasText(userDto.getMobNo()) || !userDto.getMobNo().matches(Constants.MOBNO_REGEX)){
            throw new IllegalArgumentException("mobile number is invalid");
        }

        if(CollectionUtils.isEmpty(userDto.getRoles())){
            throw new IllegalArgumentException("role is invalid");
        }
        else{
            List<Integer> roleIds = roleRepository.findAll().stream().map(r-> r.getId()).toList();

            List<Integer> invalidReqRoleIds = userDto.getRoles().stream().map(r->r.getId())
                    .filter(roleId -> !roleIds.contains(roleId))
                    .toList();

            if(!CollectionUtils.isEmpty(invalidReqRoleIds)){
                throw new IllegalArgumentException("role is invalid " + invalidReqRoleIds);
            }
        }
    }

}
