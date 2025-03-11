package com.backend.UniErrands.repository;

import com.backend.UniErrands.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.requester.role = :role")
    List<Task> findTasksByRole(String role);


    @Query("SELECT t FROM Task t WHERE " +
       "(:category IS NULL OR t.category = :category) AND " + // Use Task.Category instead of String
       "(:urgency IS NULL OR t.urgency = :urgency) AND " + 
       "(:min_price IS NULL OR t.reward >= :min_price) AND " +  // Minimum price condition
       "(:max_price IS NULL OR t.reward <= :max_price) AND " + 
       "(:distance IS NULL OR t.location <= :distance)")
List<Task> findFilteredTasks(Task.Category category, Task.Urgency urgency, Double min_price, Double max_price, Double distance);

}
