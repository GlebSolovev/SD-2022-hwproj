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
        val checker = storage.getAssignment(request.assignmentId).checkerProgram
        if (checker != null) messageBroker.sendCheckTask(
            SubmissionCheckTask(
                CheckerProgram(checker),
                request.submissionLink
            )
        ) { }
        return SubmitSubmissionResponse(storage.createSubmission(request.assignmentId, request.submissionLink))
    }
}
