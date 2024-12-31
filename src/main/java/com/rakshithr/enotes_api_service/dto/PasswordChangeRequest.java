package com.rakshithr.enotes_api_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeRequest {

    private String oldPassword;

    private String newPassword;

}
