package com.acme.center.platform.learning.interfaces.rest.resources;

public record RequestEnrollmentResource(String studentRecordId, Long courseId) {
    // This record represents the data required to request an enrollment in a course.
    // It includes the student's record ID and the ID of the course they wish to enroll in.
    public RequestEnrollmentResource {
        // The constructor is automatically generated for records, so no additional code is needed here.
        if (studentRecordId == null || studentRecordId.isBlank())
            throw new IllegalArgumentException("studentRecordId cannot be null");
        if (courseId == null || courseId <= 0)
            throw  new IllegalArgumentException("courseId cannot be null or less than 1");
    }
}
