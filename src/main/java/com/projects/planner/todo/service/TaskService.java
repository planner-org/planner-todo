package com.projects.planner.todo.service;

import com.projects.planner.entity.Task;
import com.projects.planner.todo.dto.TaskSearchDto;
import com.projects.planner.todo.repo.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task findById(Long id) {
        return taskRepository.findById(id).get();
    }

    public List<Task> findByUserId(Long userId) {
        return taskRepository.findByUserIdOrderByTitleAsc(userId);
    }

    public Page<Task> findByParams(TaskSearchDto taskSearchDto, PageRequest pageRequest) {
        return taskRepository.findByParams(taskSearchDto.getTitle(),
                taskSearchDto.getCompleted(),
                taskSearchDto.getPriorityId(),
                taskSearchDto.getCategoryId(),
                taskSearchDto.getUserId(),
                taskSearchDto.getDateFrom(),
                taskSearchDto.getDateTo(),
                pageRequest
        );
    }

    public Task add(Task task) {
        return taskRepository.save(task);
    }

    public Task update(Task task) {
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
