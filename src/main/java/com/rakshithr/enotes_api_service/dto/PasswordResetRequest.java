package com.rakshithr.enotes_api_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetRequest {

    private Integer uid;

    private String newPassword;

}
