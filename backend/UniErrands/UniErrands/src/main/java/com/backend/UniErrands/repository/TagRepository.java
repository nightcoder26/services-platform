package com.backend.UniErrands.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.UniErrands.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String tagName);
}
