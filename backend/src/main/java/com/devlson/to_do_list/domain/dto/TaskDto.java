package com.devlson.to_do_list.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Tarefa", description = "Dados de uma tarefa")
public class TaskDto {

    @Schema(
            description = "ID da tarefa.",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY  // shown in response, hidden in request
    )
    private Long id;

    @Schema(
            description = "Descrição da tarefa.",
            example = "Estudar Spring Boot"
    )
    @NotBlank(message = "A descrição não pode ser vazia.")
    private String description;

    @Schema(
            description = "Data e hora de criação da tarefa.",
            example = "2026-01-01T00:00:00.0113877",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime created_at;

    @Schema(
            description = "Status da tarefa. Se a tarefa foi concluída.",
            example = "false",
            defaultValue = "false",
            accessMode = Schema.AccessMode.READ_WRITE
    )
    private Boolean done = false;
}
