package com.rakshithr.enotes_api_service.service;

import com.rakshithr.enotes_api_service.dto.PasswordChangeRequest;

public interface UserService {

    void changePassword(PasswordChangeRequest passwordRequest);

}
