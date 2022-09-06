package com.projects.planner.todo.repo;

import com.projects.planner.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE " +
            "(:title is null or :title='' " +
            "or lower(c.title) like lower(concat('%', :title, '%'))) " +
            "and c.userId=:id " +
            "order by c.title asc ")
    List<Category> findByTitle(@Param("title") String title, @Param("id") Long userId);

    List<Category> findByUserIdOrderByTitleAsc(Long userId);
}
