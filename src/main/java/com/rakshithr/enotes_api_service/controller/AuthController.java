package com.rakshithr.enotes_api_service.controller;

import com.rakshithr.enotes_api_service.dto.UserDto;
import com.rakshithr.enotes_api_service.repository.UserRepository;
import com.rakshithr.enotes_api_service.service.UserService;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws Exception {
        Boolean register = userService.register(userDto);

        if(register){
            return CommonUtil.createBuildResponseMessage("register success", HttpStatus.CREATED);
        }

        return CommonUtil.createErrorResponseMessage("register failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
