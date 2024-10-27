package com.rakshithr.enotes_api_service.service;

import com.rakshithr.enotes_api_service.dto.NotesDto;

import java.util.List;

public interface NotesService {

    Boolean saveNotes(NotesDto notesDto) throws Exception;

    List<NotesDto> getAllNotes();

}
