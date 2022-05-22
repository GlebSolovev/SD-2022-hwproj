package ru.hse.sd.hwproj.models

import ru.hse.sd.hwproj.utils.CheckerProgram
import ru.hse.sd.hwproj.utils.Timestamp


interface RequestModel

class ListAssignmentsRequest : RequestModel

data class SubmitSubmissionRequest(val assignmentId: Int, val submissionLink: String) : RequestModel

class ListSubmissionsRequest : RequestModel

data class GetSubmissionDetailsRequest(val submissionId: Int) : RequestModel

data class CreateAssignmentRequest(
    val name: String,
    val taskText: String,
    val publicationTimestamp: Timestamp,
    val deadlineTimestamp: Timestamp,
    val checker: CheckerProgram
) : RequestModel
