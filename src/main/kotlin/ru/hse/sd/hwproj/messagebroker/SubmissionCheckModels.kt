package ru.hse.sd.hwproj.messagebroker

import kotlinx.serialization.Serializable
import ru.hse.sd.hwproj.utils.CheckerProgram

@Serializable
data class SubmissionCheckTask(val checker: CheckerProgram, val submissionLink: String)

@Serializable
data class SubmissionCheckResult(val checkStatus: Boolean, val outputString: String)

// enum class CheckStatus {
//    OK,
//    FAILED,
//    IN_PROGRESS
// }
