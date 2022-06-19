package ru.hse.sd.hwproj.interactor

import ru.hse.sd.hwproj.entities.*
import ru.hse.sd.hwproj.storage.Storage

/**
 * Class creating and storing all request entities.
 *
 * @param storage The [Storage] that will be used by created entities.
 */
class EntityGateway(storage: Storage) {

    /**
     * Self-explanatory.
     */
    val createAssignment = CreateAssignment(storage)

    /**
     * Self-explanatory.
     */
    val getSubmissionDetails = GetSubmissionDetails(storage)

    /**
     * Self-explanatory.
     */
    val listAssignments = ListAssignments(storage)

    /**
     * Self-explanatory.
     */
    val listSubmissions = ListSubmissions(storage)

    /**
     * Self-explanatory.
     */
    val submitSubmission = SubmitSubmission(storage)

    /**
     * Self-explanatory.
     */
    val getAssignmentDetails = GetAssignmentDetails(storage)
}
