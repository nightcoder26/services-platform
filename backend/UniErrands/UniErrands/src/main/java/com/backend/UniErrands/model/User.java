package com.backend.UniErrands.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@vitapstudent\\.ac\\.in$", message = "Enter a valid college email ID")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
private String role; // Roles can be REQUESTER, HELPER, or BOTH


    private String profilePicture; // URL or path to the profile picture
    private double ratings; // User ratings

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_tags", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();  // Directly store tags as strings

    public User(String username, String password, String email, String role, String profilePicture, double ratings, Set<String> tags) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.profilePicture = profilePicture;
        this.ratings = ratings;
        this.tags = tags;
    }

    public String getUserDetails() {
        return "Username: " + username +
               (email != null ? ", Email: " + email : "") +
               (role != null ? ", Role: " + role : "") +
               (profilePicture != null ? ", Profile Picture: " + profilePicture : "") +
               (ratings > 0 ? ", Ratings: " + ratings : "") +
               (tags != null && !tags.isEmpty() ? ", Tags: " + String.join(", ", tags) : "");
    }
}
