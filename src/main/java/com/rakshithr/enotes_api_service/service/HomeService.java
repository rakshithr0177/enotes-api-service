package com.rakshithr.enotes_api_service.service;

public interface HomeService {
     Boolean verifyAccount(Integer userId, String verificationCode) throws Exception;

}
