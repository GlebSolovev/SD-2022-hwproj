package ru.hse.sd.hwproj.entities

import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.storage.Storage

class ListAssignments(private val storage: Storage) : AbstractEntity<ListAssignmentsRequest>() {

    override fun execute(request: ListAssignmentsRequest): ListAssignmentsResponse =
        ListAssignmentsResponse(storage.listAssignments().map { AssignmentResponse(it) })
}
