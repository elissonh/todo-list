package com.devlson.to_do_list;

import com.devlson.to_do_list.domain.entities.TaskEntity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class TestDataUtils {
    private TestDataUtils() {}

    public static TaskEntity createTestTask() {
        return TaskEntity.builder()
                .description("Beber agua")
                .created_at(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .done(false)
                .build();
    }

    public static TaskEntity createTestTaskWithEmptyDescription(String invalidDescription) {
        return TaskEntity.builder()
                .description(invalidDescription)
                .created_at(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .done(false)
                .build();
    }

    public static TaskEntity createTestTaskB() {
        return TaskEntity.builder()
                .description("Ler livro")
                .created_at(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .done(true)
                .build();
    }
}
