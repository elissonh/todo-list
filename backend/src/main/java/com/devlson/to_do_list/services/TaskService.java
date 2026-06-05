package com.devlson.to_do_list.services;

import com.devlson.to_do_list.domain.dto.TaskDto;
import com.devlson.to_do_list.domain.entities.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    TaskEntity createTask(TaskEntity task);

    List<TaskEntity> findAll();

    Page<TaskEntity> findAll(Pageable pageable);

    Optional<TaskEntity> findOne(Long id);

    boolean exists(Long id);

    TaskEntity patchTask(Long id, TaskEntity taskEntity);

    void delete(Long id);
}
