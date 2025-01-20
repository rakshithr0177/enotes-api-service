package com.rakshithr.enotes_api_service.config;

import com.rakshithr.enotes_api_service.entity.User;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;


public class AuditAwareConfig implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        User loggedInUser = CommonUtil.getLoggInUser();
        return Optional.of(loggedInUser.getId());
    }
}
