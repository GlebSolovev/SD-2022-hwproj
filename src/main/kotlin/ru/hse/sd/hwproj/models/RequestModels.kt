@file:UseSerializers(TimestampSerializer::class)

package ru.hse.sd.hwproj.models

import kotlinx.serialization.UseSerializers

import kotlinx.serialization.Serializable
import ru.hse.sd.hwproj.utils.CheckerProgram
import ru.hse.sd.hwproj.utils.Timestamp
import ru.hse.sd.hwproj.utils.TimestampSerializer

@Serializable
sealed class RequestModel

@Serializable
class ListAssignmentsRequest : RequestModel()

@Serializable
data class SubmitSubmissionRequest(val assignmentId: Int, val submissionLink: String) : RequestModel()

@Serializable
class ListSubmissionsRequest : RequestModel()

@Serializable
data class GetSubmissionDetailsRequest(val submissionId: Int) : RequestModel()

@Serializable
data class CreateAssignmentRequest(
    val name: String,
    val taskText: String,
    val publicationTimestamp: Timestamp,
    val deadlineTimestamp: Timestamp,
    val checker: CheckerProgram?
) : RequestModel()
