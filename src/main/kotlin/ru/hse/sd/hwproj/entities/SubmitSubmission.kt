package ru.hse.sd.hwproj.entities

import ru.hse.sd.hwproj.messagebroker.EntityMessageBroker
import ru.hse.sd.hwproj.messagebroker.SubmissionCheckTask
import ru.hse.sd.hwproj.models.SubmitSubmissionRequest
import ru.hse.sd.hwproj.models.SubmitSubmissionResponse
import ru.hse.sd.hwproj.storage.Storage
import ru.hse.sd.hwproj.utils.CheckerProgram

/**
 * Student request for submitting a new submission.
 */
class SubmitSubmission(private val storage: Storage, private val messageBroker: EntityMessageBroker) :
    AbstractEntity<SubmitSubmissionRequest>() {

    override fun execute(request: SubmitSubmissionRequest): SubmitSubmissionResponse {
        val submissionId = storage.createSubmission(request.assignmentId, request.submissionLink)

        val checker = storage.getAssignment(request.assignmentId).checkerProgram
        if (checker != null) messageBroker.sendCheckTask(
            SubmissionCheckTask(
                submissionId,
                CheckerProgram(checker),
                request.submissionLink
            )
        ) { checkStatus ->
            println("SAVE CHECK STATUS: $checkStatus")
            // TODO: replace with storage.getSubmission(submissionId).checkResult = checkStatus.status
        }

        return SubmitSubmissionResponse(submissionId)
    }
}
