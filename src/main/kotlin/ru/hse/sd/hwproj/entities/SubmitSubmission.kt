package ru.hse.sd.hwproj.entities

import ru.hse.sd.hwproj.models.SubmitSubmissionRequest
import ru.hse.sd.hwproj.models.SubmitSubmissionResponse
import ru.hse.sd.hwproj.storage.Storage

/**
 * Student request for submitting a new submission.
 */
class SubmitSubmission(private val storage: Storage) : AbstractEntity<SubmitSubmissionRequest>() {

    override fun execute(request: SubmitSubmissionRequest): SubmitSubmissionResponse =
        SubmitSubmissionResponse(storage.createSubmission(request.assignmentId, request.submissionLink))
}
