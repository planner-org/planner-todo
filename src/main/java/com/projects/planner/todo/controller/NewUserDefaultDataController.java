package com.projects.planner.todo.controller;

import com.projects.planner.todo.service.NewUserDefaultDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class NewUserDefaultDataController {

    private final NewUserDefaultDataService service;

    @PostMapping("/init")
    public ResponseEntity<Boolean> init(@RequestBody Long userId) {

        return ResponseEntity.ok(service.setDefaultUserData(userId));

    }
}
