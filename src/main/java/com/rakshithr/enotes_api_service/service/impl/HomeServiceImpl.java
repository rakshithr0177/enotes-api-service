package com.rakshithr.enotes_api_service.service.impl;

import com.rakshithr.enotes_api_service.entity.AccountStatus;
import com.rakshithr.enotes_api_service.entity.User;
import com.rakshithr.enotes_api_service.exception.ResourceNotFoundException;
import com.rakshithr.enotes_api_service.exception.SuccessException;
import com.rakshithr.enotes_api_service.repository.UserRepository;
import com.rakshithr.enotes_api_service.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService {

    private final UserRepository userRepository;

    @Override
    public Boolean verifyAccount(Integer userId, String verificationCode) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("invalid user"));

        if(user.getStatus().getVerificationCode() == null){
            throw new SuccessException("account already verified");
        }

        if(user.getStatus().getVerificationCode().equals(verificationCode)){
            AccountStatus status = user.getStatus();
            status.setIsActive(true);
            status.setVerificationCode(null);

            userRepository.save(user);

            return true;
        }

        return false;
    }
}
