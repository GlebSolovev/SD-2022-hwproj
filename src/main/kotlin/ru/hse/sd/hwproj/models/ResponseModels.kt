package ru.hse.sd.hwproj.models

import ru.hse.sd.hwproj.storage.AssignmentORM
import ru.hse.sd.hwproj.storage.CheckResultORM
import ru.hse.sd.hwproj.storage.SubmissionORM
import ru.hse.sd.hwproj.utils.Timestamp


interface ResponseModel

data class ListAssignmentsResponse(val assignments: List<AssignmentResponse>) : ResponseModel

data class SubmitSubmissionResponse(val submissionId: Int) : ResponseModel

data class ListSubmissionsResponse(val submissions: List<SubmissionResponse>) : ResponseModel

sealed class GetSubmissionDetailsResponse: ResponseModel

data class GetExistingSubmissionDetailsResponse(
    val submissionResponse: SubmissionResponse,
    val checkResultResponse: CheckResultResponse
) : GetSubmissionDetailsResponse()

object GetAbsentSubmissionDetailsResponse : GetSubmissionDetailsResponse()

data class CreateAssignmentResponse(val assignmentId: Int) : ResponseModel

data class AssignmentResponse(val name: String, val deadlineTimestamp: Timestamp, val id: Int) {

    constructor(assignmentORM: AssignmentORM) : this(
        assignmentORM.name,
        assignmentORM.deadlineTimestamp,
        assignmentORM.id
    )

}

data class SubmissionResponse(val success: Boolean, val assignmentName: String, val id: Int, val timestamp: Timestamp) {

    constructor(submissionORM: SubmissionORM) : this(
        submissionORM.checkResult.success,
        submissionORM.assignment.name,
        submissionORM.id,
        submissionORM.submissionTimestamp
    )

}

data class CheckResultResponse(val success: Boolean, val output: String) {

    constructor(checkResultORM: CheckResultORM) : this(checkResultORM.success, checkResultORM.output)

}