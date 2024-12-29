package com.rakshithr.enotes_api_service.service;

import com.rakshithr.enotes_api_service.dto.LoginRequest;
import com.rakshithr.enotes_api_service.dto.LoginResponse;
import com.rakshithr.enotes_api_service.dto.UserDto;

public interface UserService {

    Boolean register(UserDto userDto, String url) throws Exception;

    LoginResponse login(LoginRequest loginRequest);
}
