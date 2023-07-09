package com.projects.planner.todo.controller;

import com.projects.planner.entity.Priority;
import com.projects.planner.entity.User;
import com.projects.planner.todo.dto.PrioritySearchDto;
import com.projects.planner.todo.feign.UserFeignClient;
import com.projects.planner.todo.service.PriorityService;
import com.projects.planner.utils.Checker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/priority")
@RequiredArgsConstructor
@Log4j2
public class PriorityController {

    private final PriorityService priorityService;
    private final UserFeignClient userFeignClient;

    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority) {

        try {
            Checker.idNotNull(priority.getId());
            Checker.paramIsNullOrEmpty(priority.getTitle(), "TITLE");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
//        With WebClient
//        if (userWebClientBuilder.userExists(priority.getUserId())) {
//            return ResponseEntity.ok(priorityService.add(priority));
//        }

//        With Feign
        ResponseEntity<User> response = userFeignClient.findUserById(priority.getUserId());

        if (response == null) {
            return new ResponseEntity("Система пользователей недоступна, попробуйте позже", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getBody() != null) {
            return ResponseEntity.ok(priorityService.add(priority));
        }

        return new ResponseEntity("UserId = " + priority.getUserId() + " not found!", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Priority priority) {

        try {
            Checker.idIsNullOrZero(priority.getId());
            Checker.paramIsNullOrEmpty(priority.getTitle(), "TITLE");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

        priorityService.update(priority);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {

        try {
            Checker.idIsNullOrZero(id);
            priorityService.delete(id);
        } catch (IllegalArgumentException argEx) {
            return new ResponseEntity(argEx.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (EmptyResultDataAccessException e) {
            String message = "Priority with id = " + id + " not found";
            log.error(message);
            return new ResponseEntity(message, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchDto prioritySearchDto) {

        try {
            Checker.idIsNullOrZero(prioritySearchDto.getUserId());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

        List<Priority> categories = priorityService.findByTitle(prioritySearchDto.getTitle(), prioritySearchDto.getUserId());

        return ResponseEntity.ok(categories);
    }

    @PostMapping("/id")
    public ResponseEntity<Priority> getById(@RequestBody Long id) {

        try {
            Checker.idIsNullOrZero(id);
            return ResponseEntity.ok(priorityService.findById(id));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (NoSuchElementException e) {
            String message = "Priority with id = " + id + " not found";
            log.error(message);
            return new ResponseEntity(message, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @PostMapping("/all")
    public ResponseEntity<List<Priority>> getAll(@RequestBody Long userId) {
        try {
            Checker.idIsNullOrZero(userId);
            return ResponseEntity.ok(priorityService.findAll(userId));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    
}
