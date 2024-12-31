package com.rakshithr.enotes_api_service.service.impl;

import com.rakshithr.enotes_api_service.dto.PasswordChangeRequest;
import com.rakshithr.enotes_api_service.entity.User;
import com.rakshithr.enotes_api_service.repository.UserRepository;
import com.rakshithr.enotes_api_service.service.UserService;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public void changePassword(PasswordChangeRequest passwordRequest) {
        User loggedInUser = CommonUtil.getLoggInUser();

        if(!passwordEncoder.matches(passwordRequest.getOldPassword(), loggedInUser.getPassword())){
            throw new IllegalArgumentException("old password is incorrect !!");
        }
        String encodePassword = passwordEncoder.encode(passwordRequest.getNewPassword());
        loggedInUser.setPassword(encodePassword);
        userRepository.save(loggedInUser);
    }
}
