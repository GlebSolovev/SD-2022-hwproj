package ru.hse.sd.hwproj.entities

import ru.hse.sd.hwproj.models.GetAssignmentDetailsRequest
import ru.hse.sd.hwproj.models.GetAssignmentDetailsResponse
import ru.hse.sd.hwproj.models.ResponseModel
import ru.hse.sd.hwproj.storage.Storage

class GetAssignmentDetails(private val storage: Storage) : AbstractEntity<GetAssignmentDetailsRequest>() {
    override fun execute(request: GetAssignmentDetailsRequest): ResponseModel {
        val assignment = storage.getAssignment(request.assignmentId)
        return GetAssignmentDetailsResponse(assignment)
    }
}
