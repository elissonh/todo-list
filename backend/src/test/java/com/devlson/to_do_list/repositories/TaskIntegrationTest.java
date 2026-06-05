package com.devlson.to_do_list.repositories;

import com.devlson.to_do_list.TestDataUtils;
import com.devlson.to_do_list.domain.entities.TaskEntity;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaskIntegrationTest {

    private final TaskRepository underTest;

    @Autowired
    public TaskIntegrationTest(TaskRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testValidCreateTask() {
        TaskEntity task = TestDataUtils.createTestTask();
        underTest.save(task);
        Optional<TaskEntity> result = underTest.findById(task.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(task);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    public void testCreateTaskWithInvalidDescriptionShouldFail(String invalidDescription) {
        TaskEntity task = TestDataUtils.createTestTaskWithEmptyDescription(invalidDescription);

        assertThatThrownBy(() -> underTest.saveAndFlush(task))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void testListTasks() {
        TaskEntity taskA = TestDataUtils.createTestTask();
        TaskEntity taskB = TestDataUtils.createTestTaskB();

        underTest.save(taskA);
        underTest.save(taskB);

        Iterable<TaskEntity> result = underTest.findAll();

        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(taskA, taskB);
    }

    @Test
    public void testPartialUpdateTask() {
        TaskEntity task = TestDataUtils.createTestTask();
        underTest.save(task);

        TaskEntity partialUpdate = TaskEntity.builder()
                .description("Updated description")
                .build();

        TaskEntity existingTask = underTest.findById(task.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (partialUpdate.getDescription() != null) {
            existingTask.setDescription(partialUpdate.getDescription());
        }

        underTest.save(existingTask);

        Optional<TaskEntity> result = underTest.findById(task.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getDescription()).isEqualTo("Updated description");
        assertThat(result.get().getId()).isEqualTo(task.getId()); // id unchanged
    }

    @Test
    public void testDeleteTask() {
        TaskEntity task = TestDataUtils.createTestTask();
        underTest.save(task);

        assertThat(underTest.findById(task.getId())).isPresent();

        underTest.deleteById(task.getId());

        assertThat(underTest.findById(task.getId())).isEmpty();
    }
}
