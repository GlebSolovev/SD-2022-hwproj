package ru.hse.sd.hwproj.interactor

import ru.hse.sd.hwproj.models.*

/**
 * Class for matching a request with a corresponding entity and executing it.
 *
 * @param entityGateway The [EntityGateway] that will be used for creating entities.
 */
class Interactor(private val entityGateway: EntityGateway) {

    /**
     * Matches [request] with corresponding entity and executes it, returning the resulting [ResponseModel].
     */
    fun handleRequest(request: RequestModel): ResponseModel = when (request) {
        is CreateAssignmentRequest -> entityGateway.createAssignment.execute(request)
        is GetSubmissionDetailsRequest -> entityGateway.getSubmissionDetails.execute(request)
        is ListAssignmentsRequest -> entityGateway.listAssignments.execute(request)
        is ListSubmissionsRequest -> entityGateway.listSubmissions.execute(request)
        is SubmitSubmissionRequest -> entityGateway.submitSubmission.execute(request)
        is GetAssignmentDetailsRequest -> entityGateway.getAssignmentDetails.execute(request)
    }
}
