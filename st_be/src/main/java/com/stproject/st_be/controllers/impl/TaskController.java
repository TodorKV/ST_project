package com.stproject.st_be.controllers.impl;

import com.stproject.st_be.dto.TaskDto;
import com.stproject.st_be.services.base.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping()
    public ResponseEntity<TaskDto> save(@RequestBody TaskDto dto) {
        return new ResponseEntity<>(taskService.save(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TaskDto>> getAllItemsPaginated(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam boolean finished) {
        Page<TaskDto> list = taskService.getAllPaginated(pageNo, pageSize, sortBy, finished);

        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<Page<TaskDto>> searchByDescriptionPaginated(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String name,
            @RequestParam boolean finished) {
        Page<TaskDto> list = this.taskService.searchByDescriptionPaginated(name, pageNo, pageSize, sortBy, finished);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("tenant")
    public ResponseEntity<Page<TaskDto>> getByTenantId(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<TaskDto> list = this.taskService.getAllWhereTenantId(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("tenant/search")
    public ResponseEntity<Page<TaskDto>> searchByDescriptionPaginatedTenant(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String description) {
        Page<TaskDto> list = this.taskService.searchAllWhereTenantIdAndDescription(description, pageNo, pageSize,
                sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("finish/{id}")
    public ResponseEntity finish(@PathVariable("id") Integer id) {
        TaskDto targetTask = taskService.findById(id);
        targetTask.setCompleted(true);

        taskService.save(targetTask);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        taskService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
