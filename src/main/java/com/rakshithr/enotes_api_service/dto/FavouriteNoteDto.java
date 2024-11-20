package com.rakshithr.enotes_api_service.dto;

import com.rakshithr.enotes_api_service.entity.Notes;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavouriteNoteDto {
    private Integer id;

    private NotesDto note;

    private Integer userId;
}
