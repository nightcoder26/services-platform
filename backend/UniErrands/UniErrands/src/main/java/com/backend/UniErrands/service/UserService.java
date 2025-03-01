package com.backend.UniErrands.service;

import java.util.List;
import java.util.Optional;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.UniErrands.repository.UserRepository;
import com.backend.UniErrands.model.User;

@Service
public class UserService { 
    
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUserTagsById(Long userId, List<String> newTags) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        user.setTags(new HashSet<>(newTags)); // Directly update tags
        return userRepository.save(user);
    }

    public List<User> getUsersByTag(String tag) {
        return userRepository.findUsersByTag(tag);
    }

    public String getUserRole(Long userId) {
        return userRepository.findUserRoleById(userId).orElse(null);
    }

    public User updateUserProfile(Long userId, User updatedProfile) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (updatedProfile.getUsername() != null) user.setUsername(updatedProfile.getUsername());
            if (updatedProfile.getEmail() != null) user.setEmail(updatedProfile.getEmail());
            if (updatedProfile.getPassword() != null) user.setPassword(updatedProfile.getPassword());
            if (updatedProfile.getRole() != null) user.setRole(updatedProfile.getRole());
            return userRepository.save(user);
        }
        return null;
    }
}
