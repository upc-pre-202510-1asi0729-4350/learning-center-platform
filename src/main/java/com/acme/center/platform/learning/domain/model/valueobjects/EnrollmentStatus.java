package com.acme.center.platform.learning.domain.model.valueobjects;

/**
 * Enum representing the status of an enrollment.
 * @summary
 * This enum defines the possible states of an enrollment in a learning platform.
 * @see EnrollmentStatus#REQUESTED
 * @see EnrollmentStatus#CONFIRMED
 * @see EnrollmentStatus#REJECTED
 * @see EnrollmentStatus#CANCELLED
 * @since 1.0
 */
public enum EnrollmentStatus {
    REQUESTED,
    CONFIRMED,
    REJECTED,
    CANCELLED
}
