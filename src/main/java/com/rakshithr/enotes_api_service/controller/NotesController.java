package com.rakshithr.enotes_api_service.controller;

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
    public ResponseEntity<?> getAllNotesByUser(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ){
        Integer userId  = 1;
        NotesResponse notes = notesService.getAllNotesByUser(userId, pageNo, pageSize);

//        if(CollectionUtils.isEmpty(notes)){
//            return ResponseEntity.noContent().build();
//        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception {
        notesService.softDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("delete success", HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception {
        notesService.restoreNotes(id);
        return CommonUtil.createBuildResponseMessage("notes restore success", HttpStatus.OK);
    }

    @GetMapping("/recycle-bin")
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
        Integer userId = 1;
        List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
        if(CollectionUtils.isEmpty(notes)){
            return CommonUtil.createBuildResponseMessage("notes not available in recycle bin", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception {
        notesService.hardDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("delete success", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> emptyRecycleBin() throws Exception {
        int userId = 1;
        notesService.emptyRecycleBin(userId);
        return CommonUtil.createBuildResponseMessage("delete success", HttpStatus.OK);
    }
}
