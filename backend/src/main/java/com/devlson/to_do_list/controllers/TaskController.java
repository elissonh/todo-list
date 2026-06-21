package com.devlson.to_do_list.controllers;

import com.devlson.to_do_list.domain.dto.TaskDto;
import com.devlson.to_do_list.domain.entities.TaskEntity;
import com.devlson.to_do_list.exceptions.ErrorResponse;
import com.devlson.to_do_list.mappers.Mapper;
import com.devlson.to_do_list.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Tarefas", description = "Endpoints relacionadas ao gerenciamento de tarefas")
@RestController
public class TaskController {

    private final TaskService taskService;

    private final Mapper<TaskEntity, TaskDto> taskMapper;

    public TaskController(TaskService taskService, Mapper<TaskEntity, TaskDto> taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @Operation(
            summary = "Criar tarefa",
            description = "Cria uma nova tarefa com a descrição fornecida. A descrição não pode ser vazia ou em branco."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefa criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Descrição da tarefa vazia ou inválida.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                    "statusCode": 400,
                                    "message": "Descrição da tarefa não pode ser vazia"
                                }
                                """
                            )
                    )
            )
    })
    @PostMapping(path = "/api/tasks")
    public TaskDto createTask(
            @Valid @RequestBody TaskDto taskDto,
            HttpServletRequest request) {
        TaskEntity taskEntity = taskMapper.mapFrom(taskDto);
        TaskEntity createdTask = taskService.createTask(taskEntity);
        TaskDto createdTaskDto = taskMapper.mapTo(createdTask);

        URI location = URI.create(request.getRequestURI() + "/" + createdTaskDto.getId());
        return ResponseEntity.created(location).body(createdTaskDto).getBody();
    }

    @Operation(
            summary = "Buscar tarefa pelo ID",
            description = "Busca a tarefa com o ID fornecido."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefa encontrada.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa com determinado ID não encontrada.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping(path = "/api/tasks/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") Long id) {
        Optional<TaskEntity> task =  taskService.findOne(id);
        return task.<ResponseEntity<?>>map(taskEntity -> {
            TaskDto taskDto = taskMapper.mapTo(taskEntity);
            return new ResponseEntity<>(taskDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(
                new ErrorResponse(404, "Tarefa com id '" + id + "' não encontrada"),
                HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Listar tarefas",
            description = "Lista todas as tarefas criadas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefas encontradas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            )
    })
    @GetMapping(path = "/api/tasks")
    public Page<TaskDto> listTasks(Pageable pageable) {
        Page<TaskEntity> tasks = taskService.findAll(pageable);
        return tasks.map(taskMapper::mapTo);
    }

    @Operation(
            summary = "Alterar completamente tarefa pelo ID",
            description = "Altera todos os atributos da tarefa com o ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Informações da tarefa alteradas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa com determinado ID não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping(path = "/api/tasks/{id}")
    public ResponseEntity<?> updateTask(@PathVariable("id") Long id, @RequestBody TaskDto taskDto) {
        if(!taskService.exists(id)) {
            return new ResponseEntity<>(
                    new ErrorResponse(404, "Tarefa com id '" + id + "' não encontrada"),
                    HttpStatus.NOT_FOUND);
        }

        taskDto.setId(id);
        TaskEntity taskEntity = taskMapper.mapFrom(taskDto);
        TaskEntity savedTaskEntity = taskService.createTask(taskEntity);
        return new ResponseEntity<>(
                taskMapper.mapTo(savedTaskEntity),
                HttpStatus.OK);
    }

    @Operation(
            summary = "Alterar parcialmente tarefa pelo ID",
            description = "Altera as informações fornecidas de uma tarefa com o ID ."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Informações da tarefa alteradas.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa com determinado ID não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PatchMapping(path = "/api/tasks/{id}")
    public ResponseEntity<?> patchTask(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos a serem alterados na tarefa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Altera apenas a descrição da tarefa.",
                                            summary = "Atualizar descrição",
                                            value = """
                                                {
                                                    "description": "Nova descrição da tarefa"
                                                }"""
                                    ),
                                    @ExampleObject(
                                            name = "Altera apenas o status da tarefa(concluída ou não).",
                                            summary = "Atualizar status",
                                            value = """
                                                {
                                                    "done": true
                                                }"""
                                    )
                            }
                    )
            )
            @RequestBody TaskDto taskDto) {
        TaskEntity existingTask = taskService.findOne(id)
                .orElse(null);

        if(existingTask == null) {
            return new ResponseEntity<>(
                    new ErrorResponse(404, "Tarefa com id '" + id + "' não encontrada."),
                    HttpStatus.NOT_FOUND);
        }

        if (taskDto.getDescription() == null) {
            taskDto.setDescription(existingTask.getDescription());
        }

        taskDto.setId(id);
        TaskEntity taskEntity = taskMapper.mapFrom(taskDto);
        TaskEntity updatedTask = taskService.patchTask(id, taskEntity);

        return new ResponseEntity<>(taskMapper.mapTo(updatedTask), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletar tarefa pelo ID",
            description = "Deleta a tarefa com ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Tarefa removida com sucesso.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tarefa com determinado ID não encontrada.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping(path = "/api/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        if(!taskService.exists(id)) {
            return new ResponseEntity<>(
                    new ErrorResponse(404, "Tarefa com id '" + id + "' não encontrada."),
                    HttpStatus.NOT_FOUND);
        }

        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
