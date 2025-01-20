package com.rakshithr.enotes_api_service.repository;

import com.rakshithr.enotes_api_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByEmail(String email);

    User findByEmail(String email);
}
