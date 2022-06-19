package ru.hse.sd.hwproj.entities

import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.storage.Storage

/**
 * Common request for getting the list of all assignments, sorted by deadline.
 */
class ListAssignments(private val storage: Storage) : AbstractEntity<ListAssignmentsRequest>() {

    override fun execute(request: ListAssignmentsRequest): ListAssignmentsResponse =
        ListAssignmentsResponse(storage.listAssignments().map { AssignmentResponse(it) })
}
