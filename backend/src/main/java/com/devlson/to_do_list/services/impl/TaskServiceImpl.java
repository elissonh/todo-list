package com.devlson.to_do_list.services.impl;

import com.devlson.to_do_list.domain.entities.TaskEntity;
import com.devlson.to_do_list.repositories.TaskRepository;
import com.devlson.to_do_list.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskEntity createTask(TaskEntity task) {
        return taskRepository.save(task);
    }

    @Override
    public List<TaskEntity> findAll() {
        return StreamSupport.stream(taskRepository
                        .findAll()
                        .spliterator(),
                    false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TaskEntity> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Optional<TaskEntity> findOne(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public boolean exists(Long id) {
        return taskRepository.existsById(id);
    }

    @Override
    public TaskEntity patchTask(Long id, TaskEntity taskEntity) {
        taskEntity.setId(id);

        return taskRepository.findById(id).map(existingTask -> {
            Optional.ofNullable(taskEntity.getCreated_at()).ifPresent(existingTask::setCreated_at);
            Optional.of(taskEntity.isDone()).ifPresent(existingTask::setDone);
            Optional.ofNullable(taskEntity.getDescription()).ifPresent(existingTask::setDescription);
            return taskRepository.save(existingTask);
        }).orElseThrow(() -> new RuntimeException("Task with id " + id + " does not exist"));
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
