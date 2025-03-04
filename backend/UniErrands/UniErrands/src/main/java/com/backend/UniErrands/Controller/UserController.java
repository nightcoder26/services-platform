
package com.backend.UniErrands.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.UniErrands.service.UserService;
import com.backend.UniErrands.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
  
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }
    
    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{userId}/tags")
    public ResponseEntity<User> updateUserTags(@PathVariable Long userId, @RequestBody List<String> newTags) {
        if (newTags == null || newTags.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        User updatedUser = userService.updateUserTagsById(userId, newTags);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @GetMapping("tag/{tag}")
    public ResponseEntity<List<User>> getUsersByTag(@PathVariable String tag) {
        List<User> users = userService.getUsersByTag(tag);
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    // Fetch user role
    @GetMapping("/{userId}/role")
    public ResponseEntity<String> getUserRole(@PathVariable Long userId) {
        String role = userService.getUserRole(userId);
        return role != null ? ResponseEntity.ok(role) : ResponseEntity.notFound().build();
    }

    // Update user profile
    @PutMapping("/{userId}/profile")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long userId, @RequestBody User updatedProfile) {
        User updatedUser = userService.updateUserProfile(userId, updatedProfile);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }
}
