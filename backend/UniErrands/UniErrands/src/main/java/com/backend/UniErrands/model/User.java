package com.backend.UniErrands.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

// @Entity
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Table(name = "users")
// public class User {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(nullable = false, unique = true)
//     private String username;

//     @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//     @Column(nullable = false)
//     private String password;

//     @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@vitapstudent\\.ac\\.in$", message = "Enter a valid college email ID")
//     @Column(nullable = false, unique = true)
//     private String email;

//     @Column(nullable = false)
//     private String role; // Roles can be REQUESTER, HELPER, or BOTH

//     private double ratings; // User ratings

//     // @ManyToOne(fetch = FetchType.LAZY)
//     // @JoinColumn(name = "helper_id")
//     // private User helper; // New field for helper


//     @CollectionTable(name = "user_tags", joinColumns = @JoinColumn(name = "user_id"))
//     @Column(name = "tag")
//     private Set<String> tags = new HashSet<>();  // Directly store tags as strings



//     public User getHelper() {
//         return helper;
//     }

//     public void setHelper(User helper) {
//         this.helper = helper;
//     }

//     public String getUsername() {
//         return username;
//     }

//     public String getPassword() {
//         return password;
//     }

//     public String getEmail() {
//         return email;
//     }

//     public String getRole() {
//         return role;
//     }

//     public String getProfilePicture() {
//         return profilePicture;
//     }

//     public double getRatings() {
//         return ratings;
//     }

//     public String getUserDetails() {
//         return "Username: " + username +
//                (email != null ? ", Email: " + email : "") +
//                (role != null ? ", Role: " + role : "") +
//                (profilePicture != null ? ", Profile Picture: " + profilePicture : "") +
//                (ratings > 0 ? ", Ratings: " + ratings : "") +
//                (tags != null && !tags.isEmpty() ? ", Tags: " + String.join(", ", tags) : "");
//     }
// }
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@vitapstudent\\.ac\\.in$", message = "Enter a valid college email ID")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role; // REQUESTER, HELPER, BOTH

    private double ratings; // Average rating (can be used later)

    @ElementCollection
    @CollectionTable(name = "user_tags", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>(); // Interests or skills

    public String getUserDetails() {
        return "Username: " + username +
               (email != null ? ", Email: " + email : "") +
               (role != null ? ", Role: " + role : "") +
               (ratings > 0 ? ", Ratings: " + ratings : "") +
               (tags != null && !tags.isEmpty() ? ", Tags: " + String.join(", ", tags) : "");
    }
}
