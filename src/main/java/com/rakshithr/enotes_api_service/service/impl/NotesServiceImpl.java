package com.rakshithr.enotes_api_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakshithr.enotes_api_service.dto.NotesDto;
import com.rakshithr.enotes_api_service.dto.NotesResponse;
import com.rakshithr.enotes_api_service.entity.FileDetails;
import com.rakshithr.enotes_api_service.entity.Notes;
import com.rakshithr.enotes_api_service.exception.ResourceNotFoundException;
import com.rakshithr.enotes_api_service.repository.CategoryRepository;
import com.rakshithr.enotes_api_service.repository.FileDetailsRepository;
import com.rakshithr.enotes_api_service.repository.NotesRepository;
import com.rakshithr.enotes_api_service.service.NotesService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepo;

    private final FileDetailsRepository fileDetailsRepo;

    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepo;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public Boolean saveNotes(String notes, MultipartFile file) throws Exception{

        ObjectMapper ob = new ObjectMapper();
        NotesDto notesDto = ob.readValue(notes, NotesDto.class);

        //update notes if id is given in request
        if(!ObjectUtils.isEmpty(notesDto.getId())){
            updateNotes(notesDto, file);
        }

        // category validation
        checkCategoryExits(notesDto.getCategory());

        Notes notesMap = modelMapper.map(notesDto, Notes.class);

        FileDetails fileDetails = saveFileDetails(file);

        if(!ObjectUtils.isEmpty(fileDetails)){
            notesMap.setFileDetails(fileDetails);
        }
        else{
            if(ObjectUtils.isEmpty(notesDto.getId())){
                notesMap.setFileDetails(null);
            }
        }

        Notes savedNotes = notesRepo.save(notesMap);

        if(!ObjectUtils.isEmpty(savedNotes)){
            return true;
        }
        return false;
    }

    private void updateNotes(NotesDto notesDto, MultipartFile file) throws Exception {
        Notes existNotes = notesRepo.findById(notesDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid notes id"));

        //user not choose any file at update time
        if(ObjectUtils.isEmpty(file) && !ObjectUtils.isEmpty(existNotes.getFileDetails())){
            FileDetails fileDetails = existNotes.getFileDetails();
            notesDto.setFileDetails(modelMapper.map(fileDetails, NotesDto.FilesDto.class));
        }

    }

    private FileDetails saveFileDetails(MultipartFile file) throws IOException {
        if(!ObjectUtils.isEmpty(file) && !file.isEmpty()){
            String originalFileName = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFileName);

            List<String> extensionAllow = Arrays.asList("pdf", "xlsx", "jpg", "png", "docx");
            if(!extensionAllow.contains(extension)){
                throw new IllegalArgumentException("invalid file format : Upload only .pdf, .xlsx, .jpg, .png, .docx");
            }

            String rmdString = UUID.randomUUID().toString();
            String uploadFileName = rmdString + "." + extension;

            File saveFile = new File(uploadPath);
            if(!saveFile.exists()){
                saveFile.mkdir();
            }

            String storePath = uploadPath.concat(uploadFileName);

            //upload file
            long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
            if(upload != 0){
                FileDetails fileDetails = new FileDetails();
                fileDetails.setOriginalFileName(originalFileName);
                fileDetails.setDisplayFileName(getDisplayName(originalFileName));
                fileDetails.setFileSize(file.getSize());
                fileDetails.setUploadFileName(uploadFileName);
                fileDetails.setPath(storePath);

                FileDetails savedFileDetails = fileDetailsRepo.save(fileDetails);
                return savedFileDetails;
            }
        }
        return null;
    }

    private String getDisplayName(String originalFileName) {
        String extension = FilenameUtils.getExtension(originalFileName);
        String fileName = FilenameUtils.removeExtension(originalFileName);
        if(fileName.length() > 8){
            fileName = fileName.substring(0, 7);
        }
        fileName = fileName + "." + extension;
        return fileName;
    }

    private void checkCategoryExits(NotesDto.CategoryDto category) throws Exception {
        categoryRepo.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("category id invalid"));
    }

    @Override
    public List<NotesDto> getAllNotes() {
        return notesRepo.findAll().stream().map(notes -> modelMapper.map(notes, NotesDto.class)).toList();
    }

    @Override
    public byte[] downloadFile(FileDetails fileDetails) throws Exception {
        InputStream io = new FileInputStream(fileDetails.getPath());
        return StreamUtils.copyToByteArray(io);
    }

    @Override
    public FileDetails getFileDetails(Integer id) throws Exception {
        return fileDetailsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("File is not available"));
    }

    @Override
    public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Notes> pageNotes = notesRepo.findByCreatedBy(userId, pageable);

        List<NotesDto> notesDto = pageNotes.get().map(note->modelMapper.map(note, NotesDto.class)).toList();

        NotesResponse notes = NotesResponse.builder()
                .notes(notesDto)
                .pageNo(pageNotes.getNumber())
                .pageSize(pageNotes.getSize())
                .totalElements(pageNotes.getTotalElements())
                .totalPages(pageNotes.getTotalPages())
                .isFirst(pageNotes.isFirst())
                .isLast(pageNotes.isLast())
                .build();
        return notes;
    }
}
