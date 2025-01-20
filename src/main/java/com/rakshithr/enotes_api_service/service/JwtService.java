package com.rakshithr.enotes_api_service.service;

import com.rakshithr.enotes_api_service.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateToken(User user);

    String extractUsername(String token);

    Boolean validateToken(String token, UserDetails userDetails);

}
