package com.backend.UniErrands.service;

import java.util.List;
import java.util.Optional;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.UniErrands.repository.UserRepository;
import com.backend.UniErrands.model.User;

@Service
public class UserService { 

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with the same email already exists.");
        }
        return userRepository.save(user);
    }

    @Transactional
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public User updateUserProfile(Long userId, User updatedProfile) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (updatedProfile.getUsername() != null) user.setUsername(updatedProfile.getUsername());
            if (updatedProfile.getEmail() != null) user.setEmail(updatedProfile.getEmail());
            if (updatedProfile.getPassword() != null) user.setPassword(updatedProfile.getPassword());
            if (updatedProfile.getRole() != null) user.setRole(updatedProfile.getRole());
            if (updatedProfile.getProfilePicture() != null) user.setProfilePicture(updatedProfile.getProfilePicture());
            if (updatedProfile.getRatings() != 0) user.setRatings(updatedProfile.getRatings());
            return userRepository.save(user);
        }
        return null;
    }

    @Transactional
    public String getUserRole(Long userId) {
        return userRepository.findUserRoleById(userId).orElse(null);
    }

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public boolean deleteUserById(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public void addSampleUsers() {
        User user1 = new User();
        user1.setUsername("rohan_k");
        user1.setPassword("pass123");
        user1.setEmail("rohan.kumar@vitapstudent.ac.in");
        user1.setRole("REQUESTER");
        user1.setTags(new HashSet<>(List.of("java", "c++")));
        
        User user2 = new User();
        user2.setUsername("alice_s");
        user2.setPassword("pass456");
        user2.setEmail("alice.smith@vitapstudent.ac.in");
        user2.setRole("REQUESTER");
        user2.setTags(new HashSet<>(List.of("python", "javascript")));
        
        userRepository.save(user1);
        userRepository.save(user2);
    }
}
