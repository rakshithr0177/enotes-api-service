package com.rakshithr.enotes_api_service.service.impl;

import com.rakshithr.enotes_api_service.dto.TodoDto;
import com.rakshithr.enotes_api_service.entity.Todo;
import com.rakshithr.enotes_api_service.enums.TodoStatus;
import com.rakshithr.enotes_api_service.exception.ResourceNotFoundException;
import com.rakshithr.enotes_api_service.repository.TodoRepository;
import com.rakshithr.enotes_api_service.service.TodoService;
import com.rakshithr.enotes_api_service.util.CommonUtil;
import com.rakshithr.enotes_api_service.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;
    private final Validation validation;

    @Override
    public Boolean saveTodo(TodoDto todoDto) throws Exception {
        //validate todo status
        validation.todoValidation(todoDto);

        Todo todo = modelMapper.map(todoDto, Todo.class);
        todo.setStatusId(todoDto.getStatus().getId());

        Todo saveTodo = todoRepository.save(todo);

        if (!ObjectUtils.isEmpty(saveTodo)) {
            return true;
        }
        return false;
    }

    @Override
    public TodoDto getTodoById(Integer id) throws Exception {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("todo not found! id invalid"));
        TodoDto todoDto = modelMapper.map(todo, TodoDto.class);
        setStatus(todoDto, todo);
        return todoDto;
    }

    private void setStatus(TodoDto todoDto, Todo todo) {
        for(TodoStatus st : TodoStatus.values()){
            if(st.getId().equals(todo.getStatusId())){
                TodoDto.StatusDto statusDto = TodoDto.StatusDto.builder()
                        .id(st.getId())
                        .name(st.getName())
                        .build();
                todoDto.setStatus(statusDto);
            }
        }
    }

    @Override
    public List<TodoDto> getTodoByUser() {
        Integer userId = CommonUtil.getLoggInUser().getId();
        List<Todo> todos = todoRepository.findByCreatedBy(userId);
        return todos.stream().map(todo -> modelMapper.map(todo, TodoDto.class)).toList();
    }
}
