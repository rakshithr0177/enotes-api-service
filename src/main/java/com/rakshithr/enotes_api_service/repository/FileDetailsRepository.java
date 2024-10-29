package com.rakshithr.enotes_api_service.repository;

import com.rakshithr.enotes_api_service.entity.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDetailsRepository extends JpaRepository<FileDetails, Integer> {
}
