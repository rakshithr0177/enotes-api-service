package com.rakshithr.enotes_api_service.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoDto {

    private Integer id;

    private String title;

    private StatusDto status;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static  class StatusDto{

        private Integer id;

        private String name;
    }
}
