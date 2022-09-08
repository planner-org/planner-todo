package com.projects.planner.todo.service;

import com.projects.planner.entity.Stat;
import com.projects.planner.todo.repo.StatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StatService {

    private final StatRepository statRepository;

    public Stat findByUserId(Long userId) {
        return statRepository.findByUserId(userId);
    }
}
