package com.rakshithr.enotes_api_service.service.impl;

import com.rakshithr.enotes_api_service.dto.NotesDto;
import com.rakshithr.enotes_api_service.entity.Notes;
import com.rakshithr.enotes_api_service.exception.ResourceNotFoundException;
import com.rakshithr.enotes_api_service.repository.CategoryRepository;
import com.rakshithr.enotes_api_service.repository.NotesRepository;
import com.rakshithr.enotes_api_service.service.NotesService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepo;

    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepo;

    @Override
    public Boolean saveNotes(NotesDto notesDto) throws Exception{

        checkCategoryExits(notesDto.getCategory());

        Notes notes = modelMapper.map(notesDto, Notes.class);
        Notes savedNotes = notesRepo.save(notes);

        if(!ObjectUtils.isEmpty(savedNotes)){
            return true;
        }
        return false;
    }

    private void checkCategoryExits(NotesDto.CategoryDto category) throws Exception {
        categoryRepo.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("category id invalid"));
    }

    @Override
    public List<NotesDto> getAllNotes() {
        return notesRepo.findAll().stream().map(notes -> modelMapper.map(notes, NotesDto.class)).toList();
    }
}
