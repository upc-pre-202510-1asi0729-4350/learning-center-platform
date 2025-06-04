package com.acme.center.platform.learning.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing the student's performance metrics.
 * @summary
 * This class encapsulates the total number of completed courses and tutorials by a student.
 * It throws an exception if the values are negative or null.
 * @param totalCompletedCourses The total number of courses completed by the student.
 * @param totalCompletedTutorials The total number of tutorials completed by the student.
 * @see IllegalArgumentException
 * @since 1.0
 */
@Embeddable
public record StudentPerformanceMetricSet(Integer totalCompletedCourses, Integer totalCompletedTutorials) {

    /**
     * Default constructor initializing the metrics to zero.
     */
    public StudentPerformanceMetricSet() { this(0, 0); }

    public StudentPerformanceMetricSet {
        if (totalCompletedCourses == null || totalCompletedCourses < 0)
            throw new IllegalArgumentException("Total completed courses must be non-negative");
        if (totalCompletedTutorials == null || totalCompletedTutorials < 0)
            throw new IllegalArgumentException("Total completed tutorials must be non-negative");
    }

    /**
     * Increment the count of completed courses by one.
     * @summary
     * This method creates a new instance of StudentPerformanceMetricSet with the total completed courses incremented by one.
     * @return A new instance of StudentPerformanceMetricSet with the updated count of completed courses.
     * @since 1.0
     */
    public StudentPerformanceMetricSet incrementCompletedCourses() {
        return new StudentPerformanceMetricSet(totalCompletedCourses + 1, totalCompletedTutorials);
    }

    /**
     * Increment the count of completed tutorials by one.
     * @summary
     * This method creates a new instance of StudentPerformanceMetricSet with the total completed tutorials incremented by one.
     * @return A new instance of StudentPerformanceMetricSet with the updated count of completed tutorials.
     * @since 1.0
     */
    public StudentPerformanceMetricSet incrementCompletedTutorials() {
        return new StudentPerformanceMetricSet(totalCompletedCourses, totalCompletedTutorials + 1);
    }
}
