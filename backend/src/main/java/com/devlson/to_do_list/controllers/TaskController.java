package com.devlson.to_do_list.controllers;

import com.devlson.to_do_list.domain.dto.TaskDto;
import com.devlson.to_do_list.domain.entities.TaskEntity;
import com.devlson.to_do_list.mappers.Mapper;
import com.devlson.to_do_list.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TaskController {

    private final TaskService taskService;

    private final Mapper<TaskEntity, TaskDto> taskMapper;

    public TaskController(TaskService taskService, Mapper<TaskEntity, TaskDto> taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping(path = "/api/tasks")
    public TaskDto createTask(@RequestBody TaskDto task) {
        TaskEntity taskEntity = taskMapper.mapFrom(task);
        TaskEntity result = taskService.createTask(taskEntity);
        return taskMapper.mapTo(result);
    }

    @GetMapping(path = "/api/tasks")
    public Page<TaskDto> listTasks(Pageable pageable) {
        Page<TaskEntity> tasks = taskService.findAll(pageable);
        return tasks.map(taskMapper::mapTo);
    }

    @GetMapping(path = "/api/tasks/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable("id") Long id) {
        Optional<TaskEntity> task =  taskService.findOne(id);
        return task.map(taskEntity -> {
            TaskDto taskDto = taskMapper.mapTo(taskEntity);
            return new ResponseEntity<>(taskDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/api/tasks/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("id") Long id, @RequestBody TaskDto taskDto) {
        if(!taskService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        taskDto.setId(id);
        TaskEntity taskEntity = taskMapper.mapFrom(taskDto);
        TaskEntity savedTaskEntity = taskService.createTask(taskEntity);
        return new ResponseEntity<>(
                taskMapper.mapTo(savedTaskEntity),
                HttpStatus.OK);
    }

    @PatchMapping(path = "/api/tasks/{id}")
    public ResponseEntity<TaskDto> patchTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        // if(!taskService.exists(id)) {
        //     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // }
        TaskEntity existingTask = taskService.findOne(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (taskDto.getDescription() == null) {
            taskDto.setDescription(existingTask.getDescription());
        }

        taskDto.setId(id);
        TaskEntity taskEntity = taskMapper.mapFrom(taskDto);
        TaskEntity updatedTask = taskService.patchTask(id, taskEntity);

        return new ResponseEntity<>(taskMapper.mapTo(updatedTask), HttpStatus.OK);
    }

    @DeleteMapping(path = "/api/tasks/{id}")
    public ResponseEntity<TaskDto> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
