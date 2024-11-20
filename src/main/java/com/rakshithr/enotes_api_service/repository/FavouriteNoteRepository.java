package com.rakshithr.enotes_api_service.repository;

import com.rakshithr.enotes_api_service.entity.FavouriteNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavouriteNoteRepository extends JpaRepository<FavouriteNote, Integer> {
    List<FavouriteNote> findByUserId(int userId);
}
