package com.rakshithr.enotes_api_service.controller;

import com.rakshithr.enotes_api_service.service.HomeService;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception {
        Boolean verifyAccount = homeService.verifyAccount(uid, code);
        if(verifyAccount){
            return CommonUtil.createBuildResponseMessage("account verification success", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("invalid verification link", HttpStatus.BAD_REQUEST);
    }
}
