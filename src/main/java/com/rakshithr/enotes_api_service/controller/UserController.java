package com.rakshithr.enotes_api_service.controller;

import com.rakshithr.enotes_api_service.dto.UserResponse;
import com.rakshithr.enotes_api_service.entity.User;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final ModelMapper modelMapper;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(){
        User loggedInUser = CommonUtil.getLoggInUser();
        UserResponse userResponse = modelMapper.map(loggedInUser, UserResponse.class);
        return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);
    }

}
