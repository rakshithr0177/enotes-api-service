package com.rakshithr.enotes_api_service.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;


public class AuditAwareConfig implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        return Optional.of(1);
    }
}
