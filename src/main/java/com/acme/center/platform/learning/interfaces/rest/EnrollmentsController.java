package com.acme.center.platform.learning.interfaces.rest;

import com.acme.center.platform.learning.domain.model.commands.CancelEnrollmentCommand;
import com.acme.center.platform.learning.domain.model.commands.ConfirmEnrollmentCommand;
import com.acme.center.platform.learning.domain.model.commands.RejectEnrollmentCommand;
import com.acme.center.platform.learning.domain.model.queries.GetEnrollmentByAcmeStudentRecordIdAndCourseIdQuery;
import com.acme.center.platform.learning.domain.services.EnrollmentCommandService;
import com.acme.center.platform.learning.domain.services.EnrollmentQueryService;
import com.acme.center.platform.learning.interfaces.rest.resources.EnrollmentResource;
import com.acme.center.platform.shared.interfaces.rest.resources.MessageResource;
import com.acme.center.platform.learning.interfaces.rest.resources.RequestEnrollmentResource;
import com.acme.center.platform.learning.interfaces.rest.transform.EnrollmentResourceFromEntityAssembler;
import com.acme.center.platform.learning.interfaces.rest.transform.RequestEnrollmentCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/enrollments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Enrollments", description = "Available Enrollment Endpoints")
public class EnrollmentsController {
    private final EnrollmentCommandService enrollmentCommandService;
    private final EnrollmentQueryService enrollmentQueryService;

    public EnrollmentsController(EnrollmentCommandService enrollmentCommandService,
                                 EnrollmentQueryService enrollmentQueryService) {
        this.enrollmentCommandService = enrollmentCommandService;
        this.enrollmentQueryService = enrollmentQueryService;
    }


    @PostMapping
    @Operation(summary = "Request Enrollment", description = "Request an enrollment for a course by providing the student record ID and course ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment requested successfully", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = EnrollmentResource.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Not Found - Enrollment not found for the provided student record ID and course ID")})
    public ResponseEntity<EnrollmentResource> requestEnrollment(@RequestBody RequestEnrollmentResource resource) {
        var requestEnrollmentCommand = RequestEnrollmentCommandFromResourceAssembler.toCommandFromResource(resource);
        var enrollmentId = enrollmentCommandService.handle(requestEnrollmentCommand);
        if (enrollmentId == null || enrollmentId.equals(0L)) return ResponseEntity.badRequest().build();
        var getEnrollmentByAcmeStudentRecordIdAndCourseIdQuery = new GetEnrollmentByAcmeStudentRecordIdAndCourseIdQuery(requestEnrollmentCommand.studentRecordId(), requestEnrollmentCommand.courseId());
        var enrollment = enrollmentQueryService.handle(getEnrollmentByAcmeStudentRecordIdAndCourseIdQuery);
        if (enrollment.isEmpty()) return ResponseEntity.notFound().build();
        var requestedEnrollment = enrollment.get();
        var enrollmentResource = EnrollmentResourceFromEntityAssembler.toResourceFromEntity(requestedEnrollment);
        return ResponseEntity.ok(enrollmentResource);
    }

    @PostMapping("/{enrollmentId}/confirmations")
    @Operation(summary = "Confirm Enrollment", description = "Confirm an enrollment for a course by providing the enrollment ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid enrollment ID")
    })
    public ResponseEntity<MessageResource> confirmEnrollment(@PathVariable Long enrollmentId) {
        var confirmEnrollmentCommand = new ConfirmEnrollmentCommand(enrollmentId);
        var confirmedEnrollment = enrollmentCommandService.handle(confirmEnrollmentCommand);
        if (confirmedEnrollment == null || confirmedEnrollment.equals(0L)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new MessageResource("Enrollment confirmed successfully"));

    }

    @PostMapping("/{enrollmentId}/rejections")
    @Operation(summary = "Reject Enrollment", description = "Reject an enrollment for a course by providing the enrollment ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment rejected successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid enrollment ID")
    })
    public ResponseEntity<MessageResource> rejectEnrollment(@PathVariable Long enrollmentId) {
        var rejectEnrollmentCommand = new RejectEnrollmentCommand(enrollmentId);
        enrollmentCommandService.handle(rejectEnrollmentCommand);
        return ResponseEntity.ok(new MessageResource("Enrollment rejected: " + enrollmentId));

    }

    @PostMapping("/{enrollmentId}/cancellations")
    @Operation(summary = "Cancel Enrollment", description = "Cancel an enrollment for a course by providing the enrollment ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid enrollment ID")
    })
    public ResponseEntity<MessageResource> cancelEnrollment(@PathVariable Long enrollmentId) {
        var cancelEnrollmentCommand = new CancelEnrollmentCommand(enrollmentId);
        enrollmentCommandService.handle(cancelEnrollmentCommand);
        return ResponseEntity.ok(new MessageResource("Enrollment rejected: " + enrollmentId));

    }

}
