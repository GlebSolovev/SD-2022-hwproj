package ru.hse.sd.hwproj.entities

import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.storage.Storage

class GetSubmissionDetails(private val storage: Storage) : AbstractEntity<GetSubmissionDetailsRequest>() {

    override fun execute(request: GetSubmissionDetailsRequest): GetSubmissionDetailsResponse {
        val submission = storage.getSubmission(request.submissionId)
        return GetSubmissionDetailsResponse(
            SubmissionResponse(submission),
            submission.submissionLink,
            submission.checkResult?.let { CheckResultResponse(it) }
        )
    }

}