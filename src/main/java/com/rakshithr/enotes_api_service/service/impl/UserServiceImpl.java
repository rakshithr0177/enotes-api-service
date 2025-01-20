package com.rakshithr.enotes_api_service.service.impl;

import com.rakshithr.enotes_api_service.dto.EmailRequest;
import com.rakshithr.enotes_api_service.dto.PasswordChangeRequest;
import com.rakshithr.enotes_api_service.dto.PasswordResetRequest;
import com.rakshithr.enotes_api_service.entity.User;
import com.rakshithr.enotes_api_service.exception.ResourceNotFoundException;
import com.rakshithr.enotes_api_service.repository.UserRepository;
import com.rakshithr.enotes_api_service.service.EmailService;
import com.rakshithr.enotes_api_service.service.UserService;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final EmailService emailService;

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

    @Override
    public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception{
        User user = userRepository.findByEmail(email);
        if(ObjectUtils.isEmpty(user)){
            throw new ResourceNotFoundException("invalid email");
        }

        //Generate Password reset token
        String passwordResetToken = UUID.randomUUID().toString();
        user.getStatus().setPasswordResetToken(passwordResetToken);
        User updateUser = userRepository.save(user);

        String url = CommonUtil.getUrl(request);
        sendEmailRequest(updateUser, url);

    }

    @Override
    public void verifyPasswordResetLink(Integer uid, String code) throws Exception{
        User user = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("invalid user"));
        verifyPasswordResetToken(user.getStatus().getPasswordResetToken(), code);
    }

    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest) throws Exception {
        User user = userRepository.findById(passwordResetRequest.getUid()).orElseThrow(() -> new ResourceNotFoundException("invalid user"));
        String encodePassword = passwordEncoder.encode(passwordResetRequest.getNewPassword());
        user.setPassword(encodePassword);
        user.getStatus().setPasswordResetToken(null);
        userRepository.save(user);
    }

    private void verifyPasswordResetToken(String existToken, String requestToken) {

        // request token not null
        if(StringUtils.hasText(requestToken)){

            // password already reset
            if(!StringUtils.hasText(existToken)){
                throw new IllegalArgumentException("already password is set");
            }

            // user request token change
            if(!existToken.equals(requestToken)){
                throw new IllegalArgumentException("invalid url");
            }
        }else{
            throw new IllegalArgumentException("invalid token");
        }
    }

    private void sendEmailRequest(User user, String url) throws Exception {
        String message = "Hi, <b>[[username]]</b>" +
                "<br><p>You have requested to reset your password.</p>"
                + "<p>Click the below link to change your password;</p>"
                + "<p><a href='[[url]]' > Change my password</a></p>"
                + "<p>Ignore this email if you do remember your password or you have not made this request. </p>"
                + " <br><br>"
                + "Thanks,<br>Enotes.com";

        message = message.replace("[[username]]", user.getFirstName());
        message = message.replace("[[url]]", url + "/api/v1/home/verify-pswd-link?uid=" + user.getId() + "&&code=" + user.getStatus().getPasswordResetToken());


        EmailRequest emailRequest = EmailRequest.builder()
                .to(user.getEmail())
                .title("Password Reset")
                .subject("Password Reset Link")
                .message(message)
                .build();

        //Send password reset  to user
        emailService.sendEmail(emailRequest);
    }

}
