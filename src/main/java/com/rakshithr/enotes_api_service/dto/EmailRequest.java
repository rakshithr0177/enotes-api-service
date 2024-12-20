package com.rakshithr.enotes_api_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {

    private String to;

    private String subject;

    private String title;

    private String message;

}
