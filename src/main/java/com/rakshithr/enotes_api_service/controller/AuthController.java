package com.rakshithr.enotes_api_service.controller;

import com.rakshithr.enotes_api_service.dto.LoginRequest;
import com.rakshithr.enotes_api_service.dto.LoginResponse;
import com.rakshithr.enotes_api_service.dto.UserDto;
import com.rakshithr.enotes_api_service.repository.UserRepository;
import com.rakshithr.enotes_api_service.service.UserService;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
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
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto, HttpServletRequest request) throws Exception {
        String url = CommonUtil.getUrl(request);
        Boolean register = userService.register(userDto, url);

        if(register){
            return CommonUtil.createBuildResponseMessage("register success", HttpStatus.CREATED);
        }

        return CommonUtil.createErrorResponseMessage("register failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
        LoginResponse loginResponse = userService.login(loginRequest);
        if(ObjectUtils.isEmpty(loginResponse)){
            return CommonUtil.createErrorResponseMessage("invalid credentials", HttpStatus.BAD_REQUEST);
        }
        return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);
    }

}
