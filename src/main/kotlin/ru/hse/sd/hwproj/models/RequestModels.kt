@file:UseSerializers(TimestampSerializer::class)
@file:Suppress("CanSealedSubClassBeObject")

package ru.hse.sd.hwproj.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import ru.hse.sd.hwproj.utils.CheckerProgram
import ru.hse.sd.hwproj.utils.Timestamp
import ru.hse.sd.hwproj.utils.TimestampSerializer

/**
 * Base class for request models. One request model represents one user request.
 */
@Serializable
sealed class RequestModel

/**
 * Request for getting the list of all assignments.
 */
@Serializable
class ListAssignmentsRequest : RequestModel()

/**
 * Request for getting the details for a specific assignment.
 * 
 * @property assignmentId The id of specific assignment.
 */
@Serializable
data class GetAssignmentDetailsRequest(val assignmentId: Int) : RequestModel()

/**
 * Request for submitting a new submission.
 *
 * @property assignmentId The assignment for which this submission is designated.
 * @property submissionLink The link to submission content.
 */
@Serializable
data class SubmitSubmissionRequest(val assignmentId: Int, val submissionLink: String) : RequestModel()

/**
 * Request for getting the list of all submissions.
 */
@Serializable
class ListSubmissionsRequest : RequestModel()

/**
 * Request for getting the details of a specific submission.
 * 
 * @property submissionId The id of specific submission.
 */
@Serializable
data class GetSubmissionDetailsRequest(val submissionId: Int) : RequestModel()

/**
 * Request for creating a new assignment.
 * 
 * @property name The name of the assignment.
 * @property taskText The statement of the assignment.
 * @property publicationTimestamp The time when this assignment should be published and become available to students.
 * @property deadlineTimestamp The deadline for this assignment.
 * @property checker The checker program for this assignment.
 */
@Serializable
data class CreateAssignmentRequest(
    val name: String,
    val taskText: String,
    val publicationTimestamp: Timestamp,
    val deadlineTimestamp: Timestamp,
    val checker: CheckerProgram?
) : RequestModel()
