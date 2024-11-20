package com.rakshithr.enotes_api_service.service;

import com.rakshithr.enotes_api_service.dto.FavouriteNoteDto;
import com.rakshithr.enotes_api_service.dto.NotesDto;
import com.rakshithr.enotes_api_service.dto.NotesResponse;
import com.rakshithr.enotes_api_service.entity.FileDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotesService {

    Boolean saveNotes(String notes, MultipartFile file) throws Exception;

    List<NotesDto> getAllNotes();

    byte[] downloadFile(FileDetails fileDetails) throws Exception;

    FileDetails getFileDetails(Integer id) throws Exception;

    NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize);

    void softDeleteNotes(Integer id) throws Exception;

    void restoreNotes(Integer id) throws Exception;

    List<NotesDto> getUserRecycleBinNotes(Integer userId);

    void hardDeleteNotes(Integer id) throws Exception;

    void emptyRecycleBin(int userId);

    void favouriteNotes(Integer noteId) throws Exception;

    void unFavouriteNotes(Integer favouriteNoteId) throws Exception;

    List<FavouriteNoteDto> getUserFavouriteNotes();
}
