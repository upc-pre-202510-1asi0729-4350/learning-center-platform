package com.acme.center.platform.learning.domain.model.entities;

import com.acme.center.platform.learning.domain.model.aggregates.Course;
import com.acme.center.platform.learning.domain.model.valueobjects.TutorialId;
import com.acme.center.platform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * LearningPathItem entity
 * @summary
 * Represents an item in a learning path, which includes a course and a tutorial ID.
 * It can also link to the next item in the learning path.
 * @see Course
 * @see TutorialId
 * @since 1.0
 */
@Getter
@Entity
public class LearningPathItem extends AuditableModel {

    @ManyToOne
    @NotNull
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    private Course course;

    @NotNull
    @Embedded
    @Column(name = "tutorial_id", nullable = false)
    private TutorialId tutorialId;

    @ManyToOne
    @JoinColumn(name = "next_item_id", referencedColumnName = "id")
    private LearningPathItem nextItem;

    public LearningPathItem(Course course, TutorialId tutorialId, LearningPathItem nextItem) {
        this.course = course;
        this.tutorialId = tutorialId;
        this.nextItem = nextItem;
    }

    // Default constructor for JPA
    protected LearningPathItem() {
        // JPA requires a no-arg constructor
    }

    public void updateNextItem(LearningPathItem nextItem) {
        this.nextItem = nextItem;
    }
}
