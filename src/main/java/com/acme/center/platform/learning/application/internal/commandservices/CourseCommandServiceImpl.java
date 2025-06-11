package com.acme.center.platform.learning.application.internal.commandservices;

import com.acme.center.platform.learning.domain.model.aggregates.Course;
import com.acme.center.platform.learning.domain.model.commands.AddTutorialToCourseLearningPathCommand;
import com.acme.center.platform.learning.domain.model.commands.CreateCourseCommand;
import com.acme.center.platform.learning.domain.model.commands.DeleteCourseCommand;
import com.acme.center.platform.learning.domain.model.commands.UpdateCourseCommand;
import com.acme.center.platform.learning.domain.services.CourseCommandService;
import com.acme.center.platform.learning.infrastructure.persistence.jpa.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseCommandServiceImpl implements CourseCommandService {
    private final CourseRepository courseRepository;

    public CourseCommandServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Long handle(CreateCourseCommand command) {
        if(courseRepository.existsByTitle(command.title()))
            throw new IllegalArgumentException("Course with title '" + command.title() + "' already exists.");
        var course = new Course(command);
        try {
            courseRepository.save(course);
        } catch (Exception e) {
            throw new RuntimeException("Error saving course: " + e.getMessage(), e);
        }
        return course.getId();
    }

    @Override
    public Optional<Course> handle(UpdateCourseCommand command) {
        if(courseRepository.existsByTitleAndIdIsNot(command.title(), command.courseId()))
            throw new IllegalArgumentException("Course with title '" + command.title() + "' already exists.");
        var result = courseRepository.findById(command.courseId());
        if (result.isEmpty())
            throw new IllegalArgumentException("Course with ID '" + command.courseId() + "' does not exist.");
        var courseToUpdate = result.get();
        try {
            var updatedCourse = courseRepository.save(courseToUpdate.updateInformation(command.title(), command.description()));
            return Optional.of(updatedCourse);
        } catch (Exception e) {
            throw new RuntimeException("Error updating course: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(DeleteCourseCommand command) {
        if(!courseRepository.existsById(command.courseId()))
            throw new IllegalArgumentException("Course with ID '" + command.courseId() + "' does not exist.");
        try {
            courseRepository.deleteById(command.courseId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting course: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(AddTutorialToCourseLearningPathCommand command) {
        if (!courseRepository.existsById(command.courseId()))
            throw new IllegalArgumentException("Course with ID '" + command.courseId() + "' does not exist.");
        try {
            courseRepository.findById(command.courseId()).map(course -> {
                course.addTutorialToLearningPath(command.tutorialId());
                courseRepository.save(course);
                return course;
            });
        } catch (Exception e) {
            throw new RuntimeException("Error adding tutorial to course learning path: " + e.getMessage(), e);
        }

    }
}
