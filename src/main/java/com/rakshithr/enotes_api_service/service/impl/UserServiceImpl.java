package com.rakshithr.enotes_api_service.service.impl;

import com.rakshithr.enotes_api_service.dto.EmailRequest;
import com.rakshithr.enotes_api_service.dto.UserDto;
import com.rakshithr.enotes_api_service.entity.AccountStatus;
import com.rakshithr.enotes_api_service.entity.Role;
import com.rakshithr.enotes_api_service.entity.User;
import com.rakshithr.enotes_api_service.repository.RoleRepository;
import com.rakshithr.enotes_api_service.repository.UserRepository;
import com.rakshithr.enotes_api_service.service.UserService;
import com.rakshithr.enotes_api_service.service.EmailService;
import com.rakshithr.enotes_api_service.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final Validation validation;

    private final ModelMapper modelMapper;

    private final EmailService emailService;

    @Override
    public Boolean register(UserDto userDto, String url) throws Exception {

        validation.userValidation(userDto);

        User user = modelMapper.map(userDto, User.class);

        setRole(userDto, user);

        AccountStatus status = AccountStatus.builder()
                .isActive(false)
                .verificationCode(UUID.randomUUID().toString())
                .build();
        user.setStatus(status);

        User savedUser = userRepository.save(user);

        if(!ObjectUtils.isEmpty(savedUser)){
            //send email
            sendEmail(savedUser, url);
            return true;
        }
        return false;
    }

    private void sendEmail(User savedUser, String url) throws Exception {

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

    private void setRole(UserDto userDto, User user) {
        List<Integer> reqRoleIds = userDto.getRoles().stream().map(r -> r.getId()).toList();
        List<Role> roles = roleRepository.findAllById(reqRoleIds);
        user.setRoles(roles);
    }
}
