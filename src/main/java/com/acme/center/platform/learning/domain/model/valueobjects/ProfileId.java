package com.acme.center.platform.learning.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing a unique identifier for a user profile.
 * @summary
 * This class encapsulates the profile ID, ensuring it is a positive number.
 * It is an embeddable type used in JPA entities.
 * @param profileId the unique identifier for the user profile
 * @see IllegalArgumentException
 * @since 1.0
 */
@Embeddable
public record ProfileId(Long profileId) {
    public ProfileId {
        if (profileId == null || profileId <= 0) {
            throw new IllegalArgumentException("Profile ID must be a positive number");
        }
    }
}
