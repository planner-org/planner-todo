package com.projects.planner.todo.controller;

import com.projects.planner.entity.Stat;
import com.projects.planner.todo.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;

    @PostMapping("/stat")
    public ResponseEntity<Stat> getStat(@RequestBody Long userId) {
        return ResponseEntity.ok(statService.findByUserId(userId));
    }

}
