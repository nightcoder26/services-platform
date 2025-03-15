package com.backend.UniErrands.repository;

import org.springframework.data.repository.CrudRepository; // Change this line

import com.backend.UniErrands.model.Admin;
import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin, Long> { // Change this line

    Optional<Admin> findByEmail(String email);
}
