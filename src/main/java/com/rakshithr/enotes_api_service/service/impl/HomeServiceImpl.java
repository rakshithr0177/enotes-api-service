package com.rakshithr.enotes_api_service.service.impl;

import com.rakshithr.enotes_api_service.entity.AccountStatus;
import com.rakshithr.enotes_api_service.entity.User;
import com.rakshithr.enotes_api_service.exception.ResourceNotFoundException;
import com.rakshithr.enotes_api_service.exception.SuccessException;
import com.rakshithr.enotes_api_service.repository.UserRepository;
import com.rakshithr.enotes_api_service.service.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService {

    private final UserRepository userRepository;

    @Override
    public Boolean verifyAccount(Integer userId, String verificationCode) throws Exception {
        log.info("HomeServiceImpl : verifyAccount() : Start");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("invalid user"));

        if(user.getStatus().getVerificationCode() == null){
            log.info("message : Account already verified");
            throw new SuccessException("account already verified");
        }

        if(user.getStatus().getVerificationCode().equals(verificationCode)){
            AccountStatus status = user.getStatus();
            status.setIsActive(true);
            status.setVerificationCode(null);

            userRepository.save(user);
            log.info("message : Account verification success");
            return true;
        }

        log.info("HomeServiceImpl : verifyAccount() : End");
        return false;
    }
}
