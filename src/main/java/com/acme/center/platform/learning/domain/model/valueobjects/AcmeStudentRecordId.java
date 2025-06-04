package com.acme.center.platform.learning.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.UUID;

/**
 * Value object representing a unique identifier for a student record.
 * @summary
 * This class encapsulates the student record ID, ensuring it is not null or blank.
 * It is an embeddable type used in JPA entities.
 * @param studentRecordId the unique identifier for the student record
 * @see IllegalArgumentException
 * @since 1.0
 */
@Embeddable
public record AcmeStudentRecordId(String studentRecordId) {
    /**
     * Default constructor that generates a random UUID for the student record ID.
     * @summary
     * This constructor is used when a new student record is created without a specific ID.
     * @see UUID
     * @since 1.0
     */
    public AcmeStudentRecordId() { this(UUID.randomUUID().toString());}

    public AcmeStudentRecordId {
        if (studentRecordId == null || studentRecordId.isBlank()) {
            throw new IllegalArgumentException("Student record ID cannot be null or blank");
        }
    }
}
