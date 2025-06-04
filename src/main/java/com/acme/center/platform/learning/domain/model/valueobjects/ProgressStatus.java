package com.acme.center.platform.learning.domain.model.valueobjects;

/**
 * Enum representing the progress status of a learning activity.
 * @summary
 * This enum defines the possible states of progress in a learning activity,
 * indicating whether it has not started, has started, or has been completed.
 * @see ProgressStatus#NOT_STARTED
 * @see ProgressStatus#STARTED
 * @see ProgressStatus#COMPLETED
 */
public enum ProgressStatus {
    NOT_STARTED,
    STARTED,
    COMPLETED
}
