package com.acme.center.platform.learning.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object representing a unique identifier for a tutorial.
 * @summary
 * This class encapsulates the tutorial ID, ensuring it is a positive non-null value.
 * @see IllegalArgumentException
 * @since 1.0
 */
@Embeddable
public record TutorialId(Long tutorialId) {

    public TutorialId {
        if (tutorialId == null || tutorialId <= 0) {
            throw new IllegalArgumentException("Tutorial ID must be a positive non-null value");
        }
    }
}
