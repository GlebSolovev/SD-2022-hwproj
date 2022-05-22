package ru.hse.sd.hwproj.entities

import ru.hse.sd.hwproj.models.ListAssignmentsRequest
import ru.hse.sd.hwproj.models.ListAssignmentsResponse

class ListAssignments : AbstractEntity<ListAssignmentsRequest>() {

    override fun execute(request: ListAssignmentsRequest): ListAssignmentsResponse {
        TODO("Not yet implemented")
    }

}