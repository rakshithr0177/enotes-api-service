package com.rakshithr.enotes_api_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Integer id;

    private String firstName;

    private String lastName;

    private  String email;

    private String mobNo;

    private StatusDto status;

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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StatusDto {

        private Integer id;

        private Boolean isActive;

    }

}
