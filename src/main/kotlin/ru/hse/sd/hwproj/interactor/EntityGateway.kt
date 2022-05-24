package ru.hse.sd.hwproj.interactor

import ru.hse.sd.hwproj.entities.*
import ru.hse.sd.hwproj.storage.Storage

class EntityGateway(storage: Storage) {

    val createAssignment = CreateAssignment(storage)

    val getSubmissionDetails = GetSubmissionDetails(storage)

    val listAssignments = ListAssignments(storage)

    val listSubmissions = ListSubmissions(storage)

    val submitSubmission = SubmitSubmission(storage)

    val getAssignmentDetails = GetAssignmentDetails(storage)

}
