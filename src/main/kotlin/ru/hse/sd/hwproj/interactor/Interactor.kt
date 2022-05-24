package ru.hse.sd.hwproj.interactor

import ru.hse.sd.hwproj.models.*

class Interactor(private val entityGateway: EntityGateway) {

    fun handleRequest(request: RequestModel): ResponseModel = when (request) {
        is CreateAssignmentRequest -> entityGateway.createAssignment.execute(request)
        is GetSubmissionDetailsRequest -> entityGateway.getSubmissionDetails.execute(request)
        is ListAssignmentsRequest -> entityGateway.listAssignments.execute(request)
        is ListSubmissionsRequest -> entityGateway.listSubmissions.execute(request)
        is SubmitSubmissionRequest -> entityGateway.submitSubmission.execute(request)
        is GetAssignmentDetailsRequest -> entityGateway.getAssignmentDetails.execute(request)
    }

}