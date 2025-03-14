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
       "(:category IS NULL OR t.category = :category) AND " + 
       "(:urgency IS NULL OR t.urgency = :urgency) AND " + 
       "(:minPrice IS NULL OR t.reward >= :minPrice) AND " +  
       "(:maxPrice IS NULL OR t.reward <= :maxPrice)")
  List<Task> findFilteredTasks(Task.Category category, Task.Urgency urgency, Double minPrice, Double maxPrice, Double distance);

}
