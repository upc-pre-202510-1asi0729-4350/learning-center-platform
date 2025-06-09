package com.acme.center.platform.learning.domain.model.commands;

public record CompleteTutorialForEnrollmentCommand(Long enrollmentId, Long tutorialId) {
    public CompleteTutorialForEnrollmentCommand {
        if (enrollmentId == null || enrollmentId <= 0) {
            throw new IllegalArgumentException("Enrollment ID must be a positive number");
        }
        if (tutorialId == null || tutorialId <= 0) {
            throw new IllegalArgumentException("Tutorial ID must be a positive number");
        }
    }
}
