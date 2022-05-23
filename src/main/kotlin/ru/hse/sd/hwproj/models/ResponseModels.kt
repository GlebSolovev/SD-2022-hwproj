@file:UseSerializers(TimestampSerializer::class)

package ru.hse.sd.hwproj.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import ru.hse.sd.hwproj.storage.AssignmentORM
import ru.hse.sd.hwproj.storage.CheckResultORM
import ru.hse.sd.hwproj.storage.SubmissionORM
import ru.hse.sd.hwproj.utils.Timestamp
import ru.hse.sd.hwproj.utils.TimestampSerializer

@Serializable
sealed class ResponseModel

@Serializable
data class ListAssignmentsResponse(val assignments: List<AssignmentResponse>) : ResponseModel()

@Serializable
data class SubmitSubmissionResponse(val submissionId: Int) : ResponseModel()

@Serializable
data class ListSubmissionsResponse(val submissions: List<SubmissionResponse>) : ResponseModel()

@Serializable
data class GetSubmissionDetailsResponse(
    val submissionResponse: SubmissionResponse,
    val checkResultResponse: CheckResultResponse?
) : ResponseModel()

@Serializable
data class CreateAssignmentResponse(val assignmentId: Int) : ResponseModel()

@Serializable
data class AssignmentResponse(
    val name: String,
    val deadlineTimestamp: Timestamp,
    val id: Int
) {

    constructor(assignmentORM: AssignmentORM) : this(
        assignmentORM.name,
        assignmentORM.deadlineTimestamp,
        assignmentORM._id
    )

}

@Serializable
data class SubmissionResponse(
    val success: Boolean?,
    val assignmentName: String,
    val id: Int,
    val timestamp: Timestamp
) {

    constructor(submissionORM: SubmissionORM) : this(
        submissionORM.checkResult?.success,
        submissionORM.assignment.name,
        submissionORM._id,
        submissionORM.submissionTimestamp
    )

}

@Serializable
data class CheckResultResponse(val success: Boolean, val output: String) {
    constructor(checkResultORM: CheckResultORM) : this(checkResultORM.success, checkResultORM.output)
}
