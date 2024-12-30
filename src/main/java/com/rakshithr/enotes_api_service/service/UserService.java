package com.rakshithr.enotes_api_service.service;

import com.rakshithr.enotes_api_service.dto.LoginRequest;
import com.rakshithr.enotes_api_service.dto.LoginResponse;
import com.rakshithr.enotes_api_service.dto.UserRequest;

public interface UserService {

    Boolean register(UserRequest userRequest, String url) throws Exception;

    LoginResponse login(LoginRequest loginRequest);
}
