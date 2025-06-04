package com.acme.center.platform.learning.domain.model.entities;

import com.acme.center.platform.learning.domain.model.aggregates.Enrollment;
import com.acme.center.platform.learning.domain.model.valueobjects.ProgressStatus;
import com.acme.center.platform.learning.domain.model.valueobjects.TutorialId;
import com.acme.center.platform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Getter
@Entity
public class ProgressRecordItem extends AuditableModel {

    @ManyToOne
    @JoinColumn(name = "enrollment_id", referencedColumnName = "id", nullable = false)
    private Enrollment enrollment;

    @Embedded
    @Column(name = "tutorial_id", nullable = false)
    private TutorialId tutorialId;

    private ProgressStatus status;
    private Date startedAt;
    private Date completedAt;

    // Default constructor for JPA
    protected ProgressRecordItem() {
        // JPA requires a no-arg constructor
    }

    public ProgressRecordItem(Enrollment enrollment, TutorialId tutorialId) {
        this.enrollment = enrollment;
        this.tutorialId = tutorialId;
        this.status = ProgressStatus.NOT_STARTED;
    }

    public void start() {
        this.status = ProgressStatus.STARTED;
        this.startedAt = new Date();
    }

    public void complete() {
        this.status = ProgressStatus.COMPLETED;
        this.completedAt = new Date();
    }

    public boolean isCompleted() {
        return ProgressStatus.COMPLETED.equals(this.status);
    }
    public boolean isInProgress() {
        return ProgressStatus.STARTED.equals(this.status);
    }

    public boolean isNotStarted() {
        return ProgressStatus.NOT_STARTED.equals(this.status);
    }

    public long calculateDaysElapsed() {
        if(ProgressStatus.NOT_STARTED.equals(this.status)) return 0;
        var defaultTimeZone = ZoneId.systemDefault();
        var fromDate = this.startedAt.toInstant().atZone(defaultTimeZone);
        var toDate = Objects.isNull(this.completedAt)
                ? LocalDate.now().atStartOfDay(defaultTimeZone).toInstant()
                : this.completedAt.toInstant();
        return Duration.between(fromDate, toDate).toDays();
    }
}
