
package com.backend.UniErrands.repository;

import com.backend.UniErrands.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Method to find tasks based on filtering criteria
    @Query("SELECT t FROM Task t WHERE (:category IS NULL OR t.category = :category) " +
           "AND (:urgency IS NULL OR t.urgency = :urgency) " +
           "AND (:priceRange IS NULL OR t.reward <= :priceRange) " +
           "AND (t.isPrivate = false OR t.requester.id IN (SELECT u.id FROM User u WHERE u.id = :userId))")
    List<Task> findFilteredTasks(@Param("category") String category,
                                  @Param("urgency") String urgency,
                                  @Param("priceRange") Double priceRange,
                                  @Param("userId") Long userId);

    // Method to find tasks based on user role (requester or helper)
    @Query("SELECT t FROM Task t WHERE " +
       "(:role = 'requester' AND t.requester.id = :userId) " + // Requester sees their own tasks
       "OR (:role = 'helper' AND (t.helper.id = :userId OR (t.helper IS NULL AND t.isPrivate = false)))") // Helper sees assigned or public tasks
List<Task> findTasksByRole(@Param("userId") Long userId, @Param("role") String role);

}