package com.acme.center.platform.learning.domain.model.valueobjects;

import com.acme.center.platform.learning.domain.model.aggregates.Course;
import com.acme.center.platform.learning.domain.model.entities.LearningPathItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.Objects;
import java.util.function.Predicate;

import java.util.List;

@Embeddable
public class LearningPath {
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<LearningPathItem> learningPathItems;

    public LearningPath() {
        this.learningPathItems = List.of();
    }

    private LearningPathItem getFirstLearningPathItemWhere(Predicate<LearningPathItem> predicate) {
        return learningPathItems.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(null);
    }

    private LearningPathItem getLearningPathItemWithId(Long itemId) {
        return this.getFirstLearningPathItemWhere(
                item -> item.getId().equals(itemId));
    }

    public LearningPathItem getLearningPathItemWithTutorialId(Long tutorialId) {
        return this.getFirstLearningPathItemWhere(
                item -> item.getTutorialId().tutorialId().equals(tutorialId));
    }

    public TutorialId getNextTutorialInLearningPath(TutorialId currentTutorialId) {
        LearningPathItem nextItem = getLearningPathItemWithTutorialId(currentTutorialId.tutorialId())
                .getNextItem();
        return !Objects.isNull(nextItem) ? nextItem.getTutorialId() : null;
    }

    public boolean isLastTutorialInLeaningPath(TutorialId currentTutorialId) {
        return Objects.isNull(getNextTutorialInLearningPath(currentTutorialId));
    }

    public TutorialId getFirstTutorialInLearningPath() {
        return learningPathItems.getFirst().getTutorialId();
    }
    public LearningPathItem getLastItemInLearningPath() {
        return this.getFirstLearningPathItemWhere(
                item -> Objects.isNull(item.getNextItem()));
    }

    public boolean isEmpty() {
        return learningPathItems.isEmpty();
    }

    public void addItem(Course course, TutorialId tutorialId, LearningPathItem nextItem) {
        LearningPathItem learningPathItem = new LearningPathItem(course, tutorialId, nextItem);
        learningPathItems.add(learningPathItem);
    }

    public void addItem(Course course, TutorialId tutorialId) {
        LearningPathItem learningPathItem = new LearningPathItem(course, tutorialId, null);
        LearningPathItem originalLastItem = null;
        if (!isEmpty()) originalLastItem = getLastItemInLearningPath();
        learningPathItems.add(learningPathItem);
        if (!Objects.isNull(originalLastItem)) originalLastItem.updateNextItem(learningPathItem);
    }

    public void addItem(Course course, TutorialId tutorialId, TutorialId nextTutorialId) {
        LearningPathItem nextItem = getLearningPathItemWithTutorialId(nextTutorialId.tutorialId());
        addItem(course, tutorialId, nextItem);
    }
}
