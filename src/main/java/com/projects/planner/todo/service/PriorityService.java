package com.projects.planner.todo.service;

import com.projects.planner.entity.Priority;
import com.projects.planner.todo.repo.PriorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PriorityService {

    private final PriorityRepository priorityRepository;

    public Priority findById(Long id) {
        return priorityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Priority with id=" + id + " not found!"));
    }

    public List<Priority> findAll(Long userId) {
        return priorityRepository.findByUserIdOrderByIdAsc(userId);
    }

    public List<Priority> findByTitle(String title, Long userId) {
        return priorityRepository.findByTitle(title, userId);
    }

    public Priority add(Priority priority) {
        return priorityRepository.save(priority);
    }

    public Priority update(Priority priority) {
        return priorityRepository.save(priority);
    }

    public void delete(Long id) {
        priorityRepository.deleteById(id);
    }

}
