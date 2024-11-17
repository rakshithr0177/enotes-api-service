package com.rakshithr.enotes_api_service.schedular;

import com.rakshithr.enotes_api_service.entity.Notes;
import com.rakshithr.enotes_api_service.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class NotesSchedular{

    private final NotesRepository notesRepo;

    @Scheduled(cron = "0 0 0 * * ?")   //every day at 12 am
//    @Scheduled(cron = "* * * ? * *")    // every second
    public void deleteNotesSchedular(){
        LocalDateTime cutOffDate  = LocalDateTime.now().minusDays(7);
        List<Notes> deletedNotes = notesRepo.findAllByIsDeletedAndDeletedOnBefore(true, cutOffDate);
        notesRepo.deleteAll(deletedNotes);
    }
}
