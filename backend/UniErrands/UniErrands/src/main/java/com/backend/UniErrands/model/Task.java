package com.backend.UniErrands.model;

import jakarta.persistence.*;
import lombok.*;

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
    private Status status = Status.PENDING;

    private Boolean isPrivate = false;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "helper_id")
    private User helper;

    public enum Category {
        FOOD_DELIVERY, ERRANDS, GROCERIES, MISCELLANEOUS,MEDICINES
    }

    public enum Urgency {
        NOW, TODAY, THIS_WEEK
    }

    public enum Status {
        PENDING, ACCEPTED, COMPLETED, CANCELLED
    }
}
