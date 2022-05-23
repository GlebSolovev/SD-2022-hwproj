package ru.hse.sd.hwproj.storage

import ru.hse.sd.hwproj.utils.CheckerProgram
import ru.hse.sd.hwproj.utils.Timestamp

interface Storage {

    fun listSubmissions(): List<SubmissionORM>

    fun listAssignments(): List<AssignmentORM>

    fun getSubmission(id: Int): SubmissionORM?

    fun createSubmission(assignmentId: Int, submissionLink: String): Int

    fun createAssignment(
        name: String,
        taskText: String,
        publicationTimestamp: Timestamp,
        deadlineTimestamp: Timestamp,
        checker: CheckerProgram?
    ): Int

}

interface AssignmentORM {
    val name: String
    val taskText: String
    val publicationTimestamp: Timestamp
    val deadlineTimestamp: Timestamp
    val checkerProgram: ByteArray?

    val _id: Int
}

interface SubmissionORM {
    val submissionTimestamp: Timestamp
    val submissionLink: String

    val assignment: AssignmentORM
    val checkResult: CheckResultORM?

    val _id: Int
}

interface CheckResultORM {
    val success: Boolean
    val output: String

    val _id: Int
}