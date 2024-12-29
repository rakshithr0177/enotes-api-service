package com.rakshithr.enotes_api_service.service;

import com.rakshithr.enotes_api_service.entity.User;

public interface JwtService {
    String generateToken(User user);
}
