package com.rakshithr.enotes_api_service.service.impl;

import com.rakshithr.enotes_api_service.config.security.CustomUserDetails;
import com.rakshithr.enotes_api_service.dto.*;
import com.rakshithr.enotes_api_service.entity.AccountStatus;
import com.rakshithr.enotes_api_service.entity.Role;
import com.rakshithr.enotes_api_service.entity.User;
import com.rakshithr.enotes_api_service.repository.RoleRepository;
import com.rakshithr.enotes_api_service.repository.UserRepository;
import com.rakshithr.enotes_api_service.service.JwtService;
import com.rakshithr.enotes_api_service.service.AuthService;
import com.rakshithr.enotes_api_service.service.EmailService;
import com.rakshithr.enotes_api_service.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final Validation validation;

    private final ModelMapper modelMapper;

    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public Boolean register(UserRequest userRequest, String url) throws Exception {

        validation.userValidation(userRequest);

        User user = modelMapper.map(userRequest, User.class);

        setRole(userRequest, user);

        AccountStatus status = AccountStatus.builder()
                .isActive(false)
                .verificationCode(UUID.randomUUID().toString())
                .build();
        user.setStatus(status);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        if(!ObjectUtils.isEmpty(savedUser)){
            //send email
            emailSendForRegister(savedUser, url);
            return true;
        }
        return false;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        if(authentication.isAuthenticated()){
            CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();

            String token = jwtService.generateToken(customUserDetails.getUser());

            LoginResponse loginResponse = LoginResponse.builder()
                    .token(token)
                    .userResponse(modelMapper.map(customUserDetails.getUser(), UserResponse.class))
                    .build();
            return loginResponse;
        }
        return null;
    }

    private void emailSendForRegister(User savedUser, String url) throws Exception {

        String message = "Hi, <b>[[username]]</b>" +
                "<br> Your account registered successfully <br>"
                + "<br> Click the below link and verify & activate your account <br>"
                + "<a href='[[url]]' > Click Here </a> <br><br>"
                + "Thanks,<br>Enotes.com";

        message = message.replace("[[username]]", savedUser.getFirstName());
        message = message.replace("[[url]]", url + "/api/v1/home/verify?uid=" + savedUser.getId() + "&&code=" + savedUser.getStatus().getVerificationCode());


        EmailRequest emailRequest = EmailRequest.builder()
                .to(savedUser.getEmail())
                .title("Account Creating Confirmation")
                .subject("Account Created Success")
                .message(message)
                .build();
        emailService.sendEmail(emailRequest);
    }

    private void setRole(UserRequest userRequest, User user) {
        List<Integer> reqRoleId = userRequest.getRoles().stream().map(r -> r.getId()).toList();
        List<Role> roles = roleRepository.findAllById(reqRoleId);
        user.setRoles(roles);
    }


}
