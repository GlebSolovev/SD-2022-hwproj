package ru.hse.sd.hwproj.messagebroker

import kotlinx.serialization.Serializable
import ru.hse.sd.hwproj.utils.CheckerProgram

/**
 * Encapsulates data that is necessary to execute specific check task.
 *
 * @property submissionId The id of the submission being checked.
 * @property checker The bytes of bash script checker program.
 * @property submissionLink The link to the submitted solution to check.
 */
@Serializable
data class SubmissionCheckTask(val submissionId: Int, val checker: CheckerProgram, val submissionLink: String)

/**
 * Encapsulates data of specific check task status.
 *
 * @property submissionId The id of the submission being checked.
 * @property status The current status of check task.
 * @property outputString The stdout output of executed checker script.
 */
@Serializable
data class SubmissionCheckStatus(val submissionId: Int, val status: CheckStatus, val outputString: String?)

/**
 * Describes possible check task statuses.
 *
 * [OK]: check passed, i.e. checker script exited with zero code.
 * [FAILED]: check failed, i.e. check script exited with non-zero code.
 * [IN_PROGRESS]: check is in progress, i.e. check script hasn't finished yet.
 * [ERROR]: check failed with unexpected error, i.e. some exception occurred.
 */
enum class CheckStatus {
    OK,
    FAILED,
    IN_PROGRESS,
    ERROR
}
