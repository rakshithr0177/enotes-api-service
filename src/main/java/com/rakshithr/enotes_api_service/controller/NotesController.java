package com.rakshithr.enotes_api_service.controller;

import com.rakshithr.enotes_api_service.dto.NotesDto;
import com.rakshithr.enotes_api_service.service.NotesService;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

}
