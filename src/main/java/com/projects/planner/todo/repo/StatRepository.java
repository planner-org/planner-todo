package com.projects.planner.todo.repo;

import com.projects.planner.entity.Stat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

    Stat findByUserId(Long userId);

}
