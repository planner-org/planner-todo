package com.projects.planner.todo.controller;

import com.projects.planner.entity.Category;
import com.projects.planner.entity.User;
import com.projects.planner.todo.dto.CategorySearchDto;
import com.projects.planner.todo.feign.UserFeignClient;
import com.projects.planner.todo.service.CategoryService;
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
@RequestMapping("/category")
@RequiredArgsConstructor
@Log4j2
public class CategoryController {

    private final CategoryService categoryService;
    private final UserFeignClient userFeignClient;

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {

        try {
            Checker.idNotNull(category.getId());
            Checker.paramIsNullOrEmpty(category.getTitle(), "TITLE");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

//        With WebClient
//        if (userWebClientBuilder.userExists(category.getUserId())) {
//            return ResponseEntity.ok(categoryService.add(category));
//        }

//        With Feign
        ResponseEntity<User> response = userFeignClient.findUserById(category.getUserId());

        if (response == null) {
            return new ResponseEntity("Система пользователей недоступна, попробуйте позже", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getBody() != null) {
            return ResponseEntity.ok(categoryService.add(category));
        }

        return new ResponseEntity("UserId = " + category.getUserId() + " not found!", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Category category) {

        try {
            Checker.idIsNullOrZero(category.getId());
            Checker.paramIsNullOrEmpty(category.getTitle(), "TITLE");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

        categoryService.update(category);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {

        try {
            Checker.idIsNullOrZero(id);
            categoryService.delete(id);
        } catch (IllegalArgumentException argEx) {
            return new ResponseEntity(argEx.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (EmptyResultDataAccessException e) {
            String message = "Category with id = " + id + " not found";
            log.error(message);
            return new ResponseEntity(message, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchDto categorySearchDto) {

        try {
            Checker.idIsNullOrZero(categorySearchDto.getUserId());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

        List<Category> categories = categoryService.findByTitle(categorySearchDto.getTitle(), categorySearchDto.getUserId());

        return ResponseEntity.ok(categories);
    }

    @PostMapping("/id")
    public ResponseEntity<Category> getById(@RequestBody Long id) {

        try {
            Checker.idIsNullOrZero(id);
            return ResponseEntity.ok(categoryService.findById(id));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (NoSuchElementException e) {
            String message = "Category with id = " + id + " not found";
            log.error(message);
            return new ResponseEntity(message, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @PostMapping("/all")
    public ResponseEntity<List<Category>> getAll(@RequestBody Long userId) {
        try {
            Checker.idIsNullOrZero(userId);
            return ResponseEntity.ok(categoryService.findAll(userId));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
