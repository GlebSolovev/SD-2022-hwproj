package ru.hse.sd.hwproj.models

import ru.hse.sd.hwproj.utils.Timestamp


interface ResponseModel

data class ListAssignmentsResponse(val assignments: List<AssignmentResponse>) : ResponseModel

data class SubmitSubmissionResponse(val submissionId: Int) : ResponseModel

data class ListSubmissionsResponse(val submissions: List<SubmissionResponse>) : ResponseModel

data class GetSubmissionDetailsResponse(
    val submissionResponse: SubmissionResponse,
    val checkResultResponse: CheckResultResponse
) : ResponseModel

data class CreateAssignmentResponse(val assignmentId: Int) : ResponseModel

data class AssignmentResponse(val name: String, val deadlineTimestamp: Timestamp, val id: Int)

data class SubmissionResponse(val success: Boolean, val assignmentName: String, val id: Int, val timestamp: Timestamp)

data class CheckResultResponse(val success: Boolean, val output: String)