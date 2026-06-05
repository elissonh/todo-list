package com.devlson.to_do_list.repositories;

import com.devlson.to_do_list.domain.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>,
        PagingAndSortingRepository<TaskEntity, Long> {
}
