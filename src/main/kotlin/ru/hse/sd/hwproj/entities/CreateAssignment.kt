package ru.hse.sd.hwproj.entities

import ru.hse.sd.hwproj.models.CreateAssignmentRequest
import ru.hse.sd.hwproj.models.CreateAssignmentResponse

class CreateAssignment : AbstractEntity<CreateAssignmentRequest>() {

    override fun execute(request: CreateAssignmentRequest): CreateAssignmentResponse {
        TODO("Not yet implemented")
    }

}