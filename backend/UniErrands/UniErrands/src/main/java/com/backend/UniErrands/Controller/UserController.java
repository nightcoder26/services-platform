package com.backend.UniErrands.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.UniErrands.service.UserService;
import com.backend.UniErrands.model.User;
import com.backend.UniErrands.model.Tag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;

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

   @PostMapping("/{id}/tags")
public ResponseEntity<?> addTags(@PathVariable Long id, @RequestBody Map<String, List<String>> requestBody) {
    List<String> tags = requestBody.get("tags");
    if (tags == null || tags.isEmpty()) {
        return ResponseEntity.badRequest().body("No tags provided");
    }
    
    User updatedUser  = userService.addTagsToUser (id, new HashSet<>(tags));
    return updatedUser  != null ? ResponseEntity.ok("Tags added successfully") : ResponseEntity.notFound().build();
}
    

    @PutMapping("/{id}/tags")
    public ResponseEntity<User> updateUserTags(@PathVariable Long id, @RequestBody Set<String> tags) {
        User updatedUser = userService.updateUserTagsById(id, new ArrayList<>(tags));
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("tag/{tag}")
    public ResponseEntity<List<User>> getUsersByTag(@PathVariable String tag) {
        List<User> users = userService.getUsersByTag(tag);
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @PutMapping("/tags/{tagId}")
    public ResponseEntity<User> updateTagById(@PathVariable Long tagId, @RequestBody String newTag) {
        User updatedUser = userService.updateTagById(tagId, newTag);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<Void> removeTag(@PathVariable Long tagId) {
        userService.removeTag(tagId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<List<Tag>> getTagsByUser(@PathVariable Long id) {
        List<Tag> tags = userService.getTagsByUserId(id);
        return tags != null ? ResponseEntity.ok(tags) : ResponseEntity.notFound().build();
    }
}
