package com.backend.UniErrands.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Double reward;

    @Enumerated(EnumType.STRING)
    private Urgency urgency;

    private String location;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING; // Default status is PENDING

    private Boolean isPrivate = false; // Indicates if the task is private

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "helper_id")
    private User helper;

    @OneToMany
    private List<User> requestedHelpers = new ArrayList<>(); // New field for requested helpers

    public Task(String title, String description, Category category, Double reward, Urgency urgency, String location, User requester) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.reward = reward;
        this.urgency = urgency;
        this.location = location;
        this.requester = requester;
        this.requestedHelpers = new ArrayList<>();
    }

    public void updateStatus(Status newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Invalid status provided");
        }
        this.status = newStatus;
    }

    public enum Category {
        FOOD_DELIVERY, ERRANDS, GROCERIES, MISCELLANEOUS, MEDICINES
    }

    public enum Urgency {
        NOW, TODAY, THIS_WEEK
    }

    public enum Status {
        PENDING, ACCEPTED, COMPLETED, CANCELLED, REQUESTED, APPROVED
    }

    public void setApprovedHelperId(User helper) {
        this.helper = helper; // Set the helper directly
    }

    public String getTaskDetails() {
        String requesterDetails = requester != null ? requester.getUserDetails() : "Requester not assigned";
        String helperDetails = helper != null ? helper.getUserDetails() : "Helper not assigned";
        
        return "Title: " + title +
               ", Description: " + description +
               ", Category: " + category +
               ", Reward: " + reward +
               ", Urgency: " + urgency +
               ", Location: " + location +
               ", Status: " + status +
               ", Is Private: " + isPrivate +
               ", Requester: " + requesterDetails +
               ", Helper: " + helperDetails;
    }
}
