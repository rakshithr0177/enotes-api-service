package com.rakshithr.enotes_api_service.controller;

import com.rakshithr.enotes_api_service.dto.TodoDto;
import com.rakshithr.enotes_api_service.service.TodoService;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveTodo(@RequestBody TodoDto todoDto) throws Exception {
        Boolean saveTodo = todoService.saveTodo(todoDto);
        if(saveTodo){
            return CommonUtil.createBuildResponseMessage("todo saved success", HttpStatus.CREATED);
        }
        else{
            return CommonUtil.createErrorResponseMessage("todo not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTodoById(@PathVariable Integer id) throws Exception {
       TodoDto todo = todoService.getTodoById(id);
       return CommonUtil.createBuildResponse(todo, HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTodoByUser(@PathVariable Integer id){
        List<TodoDto> todoDtoList = todoService.getTodoByUser();
        if(CollectionUtils.isEmpty(todoDtoList)){
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(todoDtoList, HttpStatus.OK);
    }

}
