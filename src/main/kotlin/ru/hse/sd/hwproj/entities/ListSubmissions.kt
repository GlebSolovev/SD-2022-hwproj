package ru.hse.sd.hwproj.entities

import ru.hse.sd.hwproj.models.ListSubmissionsRequest
import ru.hse.sd.hwproj.models.ListSubmissionsResponse
import ru.hse.sd.hwproj.models.SubmissionResponse
import ru.hse.sd.hwproj.storage.Storage

/**
 * Common request for getting the list of all submissions.
 */
class ListSubmissions(private val storage: Storage) : AbstractEntity<ListSubmissionsRequest>() {

    override fun execute(request: ListSubmissionsRequest): ListSubmissionsResponse =
        ListSubmissionsResponse(storage.listSubmissions().map { SubmissionResponse(it) })
}
