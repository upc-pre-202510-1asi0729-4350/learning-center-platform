package com.acme.center.platform.learning.application.internal.commandservices;

import com.acme.center.platform.learning.domain.model.aggregates.Enrollment;
import com.acme.center.platform.learning.domain.model.commands.*;
import com.acme.center.platform.learning.domain.services.EnrollmentCommandService;
import com.acme.center.platform.learning.infrastructure.persistence.jpa.repositories.CourseRepository;
import com.acme.center.platform.learning.infrastructure.persistence.jpa.repositories.EnrollmentRepository;
import com.acme.center.platform.learning.infrastructure.persistence.jpa.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class EnrollmentCommandServiceImpl implements EnrollmentCommandService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public EnrollmentCommandServiceImpl(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, StudentRepository studentRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Long handle(RequestEnrollmentCommand command) {
        if(!studentRepository.existsByAcmeStudentRecordId(command.studentRecordId()))
            throw new IllegalArgumentException("Student with record ID " + command.studentRecordId() + " does not exist.");
        var course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course with ID " + command.courseId() + " does not exist."));
        try {
            var enrollment = new Enrollment(command.studentRecordId(), course);
            enrollmentRepository.save(enrollment);
            return enrollment.getId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create enrollment: " + e.getMessage(), e);
        }
    }

    @Override
    public Long handle(ConfirmEnrollmentCommand command) {
        var enrollment = enrollmentRepository.findById(command.enrollmentId());
        if (enrollment.isEmpty()) return 0L;
        var enrollmentToConfirm = enrollment.get();
        try {
            enrollmentRepository.save(enrollmentToConfirm.confirm());
            return enrollmentToConfirm.getId();
        } catch (Exception e) {
            System.out.println("Failed to confirm enrollment: " + e.getMessage());
            return 0L;
        }
    }

    @Override
    public Long handle(RejectEnrollmentCommand command) {
        var enrollment = enrollmentRepository.findById(command.enrollmentId());
        if (enrollment.isEmpty()) return 0L;
        var enrollmentToReject = enrollment.get();
        try {
            enrollmentRepository.save(enrollmentToReject.reject());
            return enrollmentToReject.getId();
        } catch (Exception e) {
            System.out.println("Failed to confirm enrollment: " + e.getMessage());
            return 0L;
        }
    }

    @Override
    public Long handle(CancelEnrollmentCommand command) {
        var enrollment = enrollmentRepository.findById(command.enrollmentId());
        if (enrollment.isEmpty()) return 0L;
        var enrollmentToCancel = enrollment.get();
        try {
            enrollmentRepository.save(enrollmentToCancel.cancel());
            return enrollmentToCancel.getId();
        } catch (Exception e) {
            System.out.println("Failed to confirm enrollment: " + e.getMessage());
            return 0L;
        }
    }

    @Override
    public Long handle(CompleteTutorialForEnrollmentCommand command) {
        return enrollmentRepository.findById(command.enrollmentId()).map(enrollment -> {
            enrollment.completeTutorial(command.tutorialId());
            enrollmentRepository.save(enrollment);
            return enrollment.getId();
        }).orElseThrow(() -> new IllegalArgumentException("Enrollment with ID " + command.enrollmentId() + " does not exist."));
    }
}
