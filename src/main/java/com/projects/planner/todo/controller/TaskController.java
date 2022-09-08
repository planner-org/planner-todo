package com.projects.planner.todo.controller;


import com.projects.planner.entity.Task;
import com.projects.planner.todo.dto.TaskSearchDto;
import com.projects.planner.todo.service.TaskService;
import com.projects.planner.todo.util.Checker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Log4j2
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/all")
    public ResponseEntity<List<Task>> getByEmail(@RequestBody Long userId) {
        return ResponseEntity.ok(taskService.findByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task) {

        try {
            Checker.idNotNullAndNotZero(task.getId());
            Checker.titleNotNullAndNotEmpty(task.getTitle());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(taskService.add(task));
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Task task) {

        try {
            Checker.idIsNullOrZero(task.getId());
            Checker.titleNotNullAndNotEmpty(task.getTitle());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

        taskService.update(task);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {

        try {
            Checker.idIsNullOrZero(id);
            taskService.delete(id);
        } catch (IllegalArgumentException argEx) {
            return new ResponseEntity(argEx.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (EmptyResultDataAccessException e) {
            String message = "Task with id = " + id + " not found";
            log.error(message);
            return new ResponseEntity(message, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/id")
    public ResponseEntity<Task> getById(@RequestBody Long id) {

        try {
            Checker.idIsNullOrZero(id);
            return ResponseEntity.ok(taskService.findById(id));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (NoSuchElementException e) {
            String message = "Task with id = " + id + " not found";
            log.error(message);
            return new ResponseEntity(message, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @PostMapping("/search")
    public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchDto taskSearchDto) {

        try {
            Checker.idIsNullOrZero(taskSearchDto.getUserId());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

        if (taskSearchDto.getDateFrom() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(taskSearchDto.getDateFrom());

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 1);
            calendar.set(Calendar.MILLISECOND, 1);

            taskSearchDto.setDateFrom(calendar.getTime());
        }

        if (taskSearchDto.getDateTo() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(taskSearchDto.getDateTo());

            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);

            taskSearchDto.setDateTo(calendar.getTime());
        }

        Sort.Direction direction = "asc".equals(taskSearchDto.getSortDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(
                taskSearchDto.getPageNumber(),
                taskSearchDto.getPageSize(),
                Sort.by(direction, taskSearchDto.getSortColumn(), "id")
        );

        Page<Task> result = taskService.findByParams(taskSearchDto, pageRequest);

        return ResponseEntity.ok(result);
    }

}
