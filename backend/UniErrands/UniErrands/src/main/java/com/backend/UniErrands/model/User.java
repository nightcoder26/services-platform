package com.backend.UniErrands.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
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

    private String profilePicture; // URL or path to the profile picture
    private double ratings; // User ratings

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_tags", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();  // Directly store tags as strings

    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
