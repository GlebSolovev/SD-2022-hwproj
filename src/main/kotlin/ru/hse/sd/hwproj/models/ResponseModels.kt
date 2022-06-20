@file:UseSerializers(TimestampSerializer::class)

package ru.hse.sd.hwproj.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import ru.hse.sd.hwproj.messagebroker.CheckStatus
import ru.hse.sd.hwproj.storage.AssignmentORM
import ru.hse.sd.hwproj.storage.CheckResultORM
import ru.hse.sd.hwproj.storage.SubmissionORM
import ru.hse.sd.hwproj.utils.Timestamp
import ru.hse.sd.hwproj.utils.TimestampSerializer

/**
 * Base class for response models. One model represents a response for one request.
 */
@Serializable
sealed class ResponseModel

/**
 * Response for [ListAssignmentsRequest].
 *
 * @property assignments The requested assignments list.
 */
@Serializable
data class ListAssignmentsResponse(val assignments: List<AssignmentResponse>) : ResponseModel()

/**
 * Response for [SubmitSubmissionRequest].
 *
 * @property submissionId The id of a newly created submission.
 */
@Serializable
data class SubmitSubmissionResponse(val submissionId: Int) : ResponseModel()

/**
 * Response for [ListSubmissionsRequest].
 *
 * @property submissions The requested submissions list.
 */
@Serializable
data class ListSubmissionsResponse(val submissions: List<SubmissionResponse>) : ResponseModel()

/**
 * Response for [GetSubmissionDetailsRequest].
 *
 * @property submissionResponse Requested submission's basic info.
 * @property submissionLink Requested submission's link to content.
 * @property checkResultResponse Requested submission's checker program result.
 */
@Serializable
data class GetSubmissionDetailsResponse(
    val submissionResponse: SubmissionResponse,
    val submissionLink: String,
    val checkResultResponse: CheckResultResponse?
) : ResponseModel()

/**
 * Response for [CreateAssignmentRequest].
 *
 * @property assignmentId The id of newly created assignment.
 */
@Serializable
data class CreateAssignmentResponse(val assignmentId: Int) : ResponseModel()

/**
 * Basic information of an assignment.
 */
@Serializable
data class AssignmentResponse(
    val name: String,
    val deadlineTimestamp: Timestamp,
    val id: Int
) {

    constructor(assignmentORM: AssignmentORM) : this(
        assignmentORM.name,
        assignmentORM.deadlineTimestamp,
        assignmentORM.assignmentId
    )
}

/**
 * Basic information of a submission.
 */
@Serializable
data class SubmissionResponse(
    val checkStatus: CheckStatus?,
    val assignmentName: String,
    val id: Int,
    val timestamp: Timestamp
) {

    constructor(submissionORM: SubmissionORM) : this(
        submissionORM.checkResult?.status,
        submissionORM.assignment.name,
        submissionORM.submissionId,
        submissionORM.submissionTimestamp
    )
}

/**
 * Basic information of a check result.
 */
@Serializable
data class CheckResultResponse(val status: CheckStatus?, val output: String?) {
    constructor(checkResultORM: CheckResultORM) : this(checkResultORM.status, checkResultORM.output)
}

/**
 * Response for [GetAssignmentDetailsRequest].
 */
@Serializable
data class GetAssignmentDetailsResponse(
    val name: String,
    val taskText: String,
    val publicationTimestamp: Timestamp,
    val deadlineTimestamp: Timestamp,
    val id: Int,
) : ResponseModel() {
    constructor(assignment: AssignmentORM) : this(
        assignment.name,
        assignment.taskText,
        assignment.publicationTimestamp,
        assignment.deadlineTimestamp,
        assignment.assignmentId
    )
}
