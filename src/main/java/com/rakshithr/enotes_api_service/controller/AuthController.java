package com.rakshithr.enotes_api_service.controller;

import com.rakshithr.enotes_api_service.dto.LoginRequest;
import com.rakshithr.enotes_api_service.dto.LoginResponse;
import com.rakshithr.enotes_api_service.dto.UserRequest;
import com.rakshithr.enotes_api_service.service.AuthService;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest, HttpServletRequest request) throws Exception {
        log.info("AuthController : registerUser() : Execution Start");
        String url = CommonUtil.getUrl(request);
        Boolean register = authService.register(userRequest, url);

        if(!register){
            log.info("Error : {}","Register failed");
            return CommonUtil.createErrorResponseMessage("register failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("AuthController : registerUser() : Execution End");
        return CommonUtil.createBuildResponseMessage("register success", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
        LoginResponse loginResponse = authService.login(loginRequest);
        if(ObjectUtils.isEmpty(loginResponse)){
            return CommonUtil.createErrorResponseMessage("invalid credentials", HttpStatus.BAD_REQUEST);
        }
        return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);
    }

}
