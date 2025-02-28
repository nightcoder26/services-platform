package com.backend.UniErrands.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.UniErrands.repository.UserRepository;
import com.backend.UniErrands.repository.TagRepository;
import com.backend.UniErrands.model.User;
import com.backend.UniErrands.model.Tag;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TagRepository tagRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findUserWithTags(id).orElse(null);
    }
    
    public User createUser(User user) {
        Set<Tag> userTags = new HashSet<>();
    
        for (Tag tag : user.getTags()) {
            Tag existingTag = tagRepository.findByTagName(tag.getTagName())
                                           .orElseGet(() -> tagRepository.save(tag)); // Use existing tag or save new
            userTags.add(existingTag);
        }
    
        user.setTags(userTags);
        return userRepository.save(user);
    }
    

    public User addTagsToUser(Long userId, Set<String> tags) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }
    
        User user = optionalUser.get();
        for (String tagName : tags) {
            Tag tag = tagRepository.findByTagName(tagName)
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setTagName(tagName);
                            return tagRepository.save(newTag);
                        });
    
            user.getTags().add(tag);
        }
        return userRepository.save(user);
    }
     
    public User updateUserTagsById(Long userId, List<String> newTags) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        user.getTags().clear(); // Remove existing tags
        
        for (String tagName : newTags) {
            Tag tag = tagRepository.findByTagName(tagName)
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setTagName(tagName);
                            return tagRepository.save(newTag);
                        });

            user.getTags().add(tag);
        }

        return userRepository.save(user);
    }

    public User updateTagById(Long tagId, String newTag) {
        Optional<Tag> optionalTag = tagRepository.findById(tagId);
        if (optionalTag.isEmpty()) {
            return null;
        }

        Tag tag = optionalTag.get();
        tag.setTagName(newTag);
        tagRepository.save(tag);
        
        return tag.getUsers().stream().findFirst().orElse(null);
    }

    public void removeTag(Long tagId) {
        tagRepository.deleteById(tagId);
    }

    public List<User> getUsersByTag(String tag) {
        return userRepository.findUsersByTag(tag);
    }

    public List<Tag> getTagsByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.map(User::getTags).map(List::copyOf).orElse(null);
    }
}
