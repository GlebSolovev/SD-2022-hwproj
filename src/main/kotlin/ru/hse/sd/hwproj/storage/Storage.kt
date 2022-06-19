package ru.hse.sd.hwproj.storage

import ru.hse.sd.hwproj.utils.CheckerProgram
import ru.hse.sd.hwproj.utils.Timestamp

/**
 * Interface for providing access to a storage of some kind.
 */
interface Storage {

    /**
     * Returns the list of stored submissions.
     */
    fun listSubmissions(): List<SubmissionORM>

    /**
     * Returns the list of stored assignments.
     */
    fun listAssignments(): List<AssignmentORM>

    /**
     * Returns the submission by its id.
     */
    fun getSubmission(id: Int): SubmissionORM

    /**
     * Returns the assignment by its id.
     */
    fun getAssignment(id: Int): AssignmentORM

    /**
     * Stores a new submission.
     *
     * @param assignmentId The assignment id for which this submission is designated.
     * @param submissionLink The link to this submission's content.
     *
     * @return The id of a newly created submission.
     */
    fun createSubmission(assignmentId: Int, submissionLink: String): Int

    /**
     * Stores a new assignment.
     *
     * @param name The name of the assignment.
     * @param taskText The statement of the assignment.
     * @param publicationTimestamp The time when this assignment should be published and become available to students.
     * @param deadlineTimestamp The deadline for this assignment.
     * @param checker The checker program for this assignment.
     *
     * @return The id of a newly created assignment.
     */
    fun createAssignment(
        name: String,
        taskText: String,
        publicationTimestamp: Timestamp,
        deadlineTimestamp: Timestamp,
        checker: CheckerProgram?
    ): Int
}

/**
 * Interface for a class which is an ORM for an assignment.
 */
interface AssignmentORM {
    val name: String
    val taskText: String
    val publicationTimestamp: Timestamp
    val deadlineTimestamp: Timestamp
    val checkerProgram: ByteArray?

    val assignmentId: Int
}

/**
 * Interface for a class which is an ORM for a submission.
 */
interface SubmissionORM {
    val submissionTimestamp: Timestamp
    val submissionLink: String

    val assignment: AssignmentORM
    val checkResult: CheckResultORM?

    val submissionId: Int
}

/**
 * Interface for a class which is an ORM for a check result.
 */
interface CheckResultORM {
    val success: Boolean
    val output: String

    val checkResultId: Int
}
