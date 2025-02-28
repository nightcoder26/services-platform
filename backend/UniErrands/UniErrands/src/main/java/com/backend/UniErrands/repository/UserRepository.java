package com.backend.UniErrands.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.backend.UniErrands.model.User;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.tags WHERE u.id = :id")
    Optional<User> findUserWithTags(@Param("id") Long id);
    
    @Query("SELECT u FROM User u JOIN u.tags t WHERE t.tagName = :tag")
List<User> findUsersByTag(@Param("tag") String tag);

   
}
