package com.rakshithr.enotes_api_service.handler;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse {

    private HttpStatus responseStatus;

    private String status;

    private String message;

    private Object data;

    public ResponseEntity<?> create(){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", status);  //  success , failed
        map.put("message", message);  // saved success
        if(!ObjectUtils.isEmpty(data)){
            map.put("data", data);
        }
        return new ResponseEntity<>(map, responseStatus);
    }
}
