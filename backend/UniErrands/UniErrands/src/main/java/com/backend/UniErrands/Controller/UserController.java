package com.backend.UniErrands.Controller;

import com.backend.UniErrands.model.User;
import com.backend.UniErrands.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backend.UniErrands.dto.LoginRequest;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get user details by ID (hide password)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setPassword(null); // hide password
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    // Register new user
    @PostMapping("/auth/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            // createdUser.setPassword(null);
            return ResponseEntity.ok(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/auth/signin")
public ResponseEntity<User> signin(@RequestBody LoginRequest loginRequest) {
    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();

    User user = userService.getUserByEmail(email);
    if (user != null && user.getPassword().equals(password)) {
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    return ResponseEntity.status(401).build(); // Unauthorized
}



    // Update user info by ID
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        if (user != null) {
            user.setPassword(null);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUserById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    

    // Get list of roles with usernames
    @Autowired
    private com.backend.UniErrands.repository.UserRepository userRepository;

    @GetMapping("/roles")
    public ResponseEntity<List<Map<String, String>>> getUserRoles() {
        List<User> users = (List<User>) userRepository.findAll();

        List<Map<String, String>> userRoles = users.stream()
            .map(user -> {
                Map<String, String> map = new HashMap<>();
                map.put("username", user.getUsername());
                map.put("role", user.getRole());
                return map;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(userRoles);
    }

}   