package ru.hse.sd.hwproj.entities

import ru.hse.sd.hwproj.models.CreateAssignmentRequest
import ru.hse.sd.hwproj.models.CreateAssignmentResponse
import ru.hse.sd.hwproj.storage.Storage

class CreateAssignment(private val storage: Storage) : AbstractEntity<CreateAssignmentRequest>() {

    override fun execute(request: CreateAssignmentRequest): CreateAssignmentResponse {
        val (name, taskText, publicationTimestamp, deadlineTimestamp, checker) = request
        return CreateAssignmentResponse(
            storage.createAssignment(
                name,
                taskText,
                publicationTimestamp,
                deadlineTimestamp,
                checker
            )
        )
    }

}