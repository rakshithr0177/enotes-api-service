package com.rakshithr.enotes_api_service.service;

import com.rakshithr.enotes_api_service.dto.PasswordChangeRequest;
import com.rakshithr.enotes_api_service.dto.PasswordResetRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    void changePassword(PasswordChangeRequest passwordRequest);

    void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception;

    void verifyPasswordResetLink(Integer uid, String code) throws Exception;

    void resetPassword(PasswordResetRequest passwordResetRequest) throws Exception;
}
