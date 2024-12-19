package com.rakshithr.enotes_api_service.service.impl;

import com.rakshithr.enotes_api_service.dto.UserDto;
import com.rakshithr.enotes_api_service.entity.Role;
import com.rakshithr.enotes_api_service.entity.User;
import com.rakshithr.enotes_api_service.repository.RoleRepository;
import com.rakshithr.enotes_api_service.repository.UserRepository;
import com.rakshithr.enotes_api_service.service.UserService;
import com.rakshithr.enotes_api_service.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final Validation validation;

    private final ModelMapper modelMapper;

    @Override
    public Boolean register(UserDto userDto) {

        validation.userValidation(userDto);

        User user = modelMapper.map(userDto, User.class);

        setRole(userDto, user);

        User savedUser = userRepository.save(user);

        if(!ObjectUtils.isEmpty(savedUser)){
            return true;
        }
        return false;
    }

    private void setRole(UserDto userDto, User user) {
        List<Integer> reqRoleIds = userDto.getRoles().stream().map(r -> r.getId()).toList();
        List<Role> roles = roleRepository.findAllById(reqRoleIds);
        user.setRoles(roles);
    }
}
