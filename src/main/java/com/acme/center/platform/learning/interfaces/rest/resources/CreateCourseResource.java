package com.acme.center.platform.learning.interfaces.rest.resources;

public record CreateCourseResource(String title, String description) {

    public CreateCourseResource {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
    }
}
