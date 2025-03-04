package com.backend.UniErrands.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    public User(Long id) {
        this.id = id;
    }
    public User() {
        // Initialize fields if necessary
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@vitapstudent\\.ac\\.in$", message = "Enter a valid college email ID")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_tags", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();  // Directly store tags as strings


}
