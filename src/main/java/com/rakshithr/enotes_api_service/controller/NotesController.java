package com.rakshithr.enotes_api_service.controller;

import com.rakshithr.enotes_api_service.dto.FavouriteNoteDto;
import com.rakshithr.enotes_api_service.dto.NotesDto;
import com.rakshithr.enotes_api_service.dto.NotesResponse;
import com.rakshithr.enotes_api_service.entity.FileDetails;
import com.rakshithr.enotes_api_service.service.NotesService;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

    private final NotesService notesService;

    @PostMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception{
        Boolean saveNotes = notesService.saveNotes(notes, file);

        if(saveNotes){
            return CommonUtil.createBuildResponseMessage("notes saved success", HttpStatus.CREATED);
        }
        else{
            return CommonUtil.createErrorResponseMessage("notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {
        FileDetails fileDetails = notesService.getFileDetails(id);
        byte[] data = notesService.downloadFile(fileDetails);

        HttpHeaders headers = new HttpHeaders();
        String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

        return ResponseEntity.ok().headers(headers).body(data);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllNotes(){
        List<NotesDto> notesDtoList = notesService.getAllNotes();

        if(CollectionUtils.isEmpty(notesDtoList)){
            return ResponseEntity.noContent().build();
        }
        else{
            return CommonUtil.createBuildResponse(notesDtoList, HttpStatus.OK);
        }
    }

    @GetMapping("/user-notes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllNotesByUser(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ){
        NotesResponse notes = notesService.getAllNotesByUser(pageNo, pageSize);
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> searchNotes(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "key", defaultValue = "") String keyword
    ){
        NotesResponse notes = notesService.getAllNotesByUserSearch(pageNo, pageSize, keyword);
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception {
        notesService.softDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("delete success", HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception {
        notesService.restoreNotes(id);
        return CommonUtil.createBuildResponseMessage("notes restore success", HttpStatus.OK);
    }

    @GetMapping("/recycle-bin")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
        List<NotesDto> notes = notesService.getUserRecycleBinNotes();
        if(CollectionUtils.isEmpty(notes)){
            return CommonUtil.createBuildResponseMessage("notes not available in recycle bin", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception {
        notesService.hardDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("delete success", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> emptyUserRecycleBin() throws Exception {
        notesService.emptyRecycleBin();
        return CommonUtil.createBuildResponseMessage("delete success", HttpStatus.OK);
    }

    @GetMapping("/fav/{noteId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws Exception {
        notesService.favouriteNotes(noteId);
        return CommonUtil.createBuildResponseMessage("notes added favourite", HttpStatus.CREATED);
    }

    @DeleteMapping("/un-fav/{favNotesId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNotesId) throws Exception {
        notesService.unFavouriteNotes(favNotesId);
        return CommonUtil.createBuildResponseMessage("remove favourite", HttpStatus.OK);
    }

    @GetMapping("/fav-note")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserFavouriteNote() throws Exception {
        List<FavouriteNoteDto> favouriteNotes = notesService.getUserFavouriteNotes();
        if(CollectionUtils.isEmpty(favouriteNotes)){
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(favouriteNotes, HttpStatus.OK);
    }

    @GetMapping("/copy/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception {
        Boolean copyNotes = notesService.copyNotes(id);
        if(copyNotes){
            return CommonUtil.createBuildResponseMessage("copied success", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("copy failed! try again", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
