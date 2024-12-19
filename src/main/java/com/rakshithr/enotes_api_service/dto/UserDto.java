package com.rakshithr.enotes_api_service.dto;

import com.rakshithr.enotes_api_service.entity.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private  String email;

    private String password;

    private String mobNo;

    private List<RoleDto> roles;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoleDto {

        private Integer id;

        private String name;

    }
}
