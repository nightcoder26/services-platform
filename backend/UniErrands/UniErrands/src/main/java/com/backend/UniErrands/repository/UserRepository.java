package com.backend.UniErrands.repository;

import com.backend.UniErrands.model.User;
import org.springframework.data.repository.CrudRepository; // Change this line

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> { // Change this line

    Optional<User> findByEmail(String email);
    Optional<String> findUserRoleById(Long id);
}
