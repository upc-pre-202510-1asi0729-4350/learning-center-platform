package com.acme.center.platform.learning.interfaces.rest.transform;

import com.acme.center.platform.learning.domain.model.aggregates.Enrollment;
import com.acme.center.platform.learning.interfaces.rest.resources.EnrollmentResource;

public class EnrollmentResourceFromEntityAssembler {
    public static EnrollmentResource toResourceFromEntity(Enrollment entity) {
        return new EnrollmentResource(
                entity.getId(),
                entity.getAcmeStudentRecordId().studentRecordId(),
                entity.getCourse().getId(),
                entity.getStatus());
    }
}
