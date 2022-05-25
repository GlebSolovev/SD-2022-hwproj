package ru.hse.sd.hwproj.io.html.student

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.io.html.makeListSubmissionsPage
import ru.hse.sd.hwproj.io.html.makeSubmissionDetailsPage

fun Application.addStudentHTMLModule(interactor: Interactor) {
    routing {
        route("/student") {
            get("/assignments") {
                val request = ListAssignmentsRequest()
                val response = interactor.handleRequest(request) as ListAssignmentsResponse
                call.respondHtml { makeStudentAssignmentsPage(response) }
            }
            get("/assignments/{id}") {
                val id = call.parameters["id"]!!.toInt()
                val request = GetAssignmentDetailsRequest(id)
                val response = interactor.handleRequest(request) as GetAssignmentDetailsResponse
                call.respondHtml { makeStudentAssignmentDetailsPage(response) }
            }
            route("/submissions") {
                get {
                    val request = ListSubmissionsRequest()
                    val response = interactor.handleRequest(request) as ListSubmissionsResponse
                    call.respondHtml { makeListSubmissionsPage(response) }
                }
                post {
                    val formParameters = call.receiveParameters()
                    val assignmentId = formParameters["assignmentId"]!!.toInt()
                    val submissionLink = formParameters["link"]!!

                    val request = SubmitSubmissionRequest(assignmentId, submissionLink)
                    val response = interactor.handleRequest(request) as SubmitSubmissionResponse
                    call.respondHtml { makeStudentSubmittedSubmissionPage(response) }
                }
                get("/{id}") {
                    val id = call.parameters["id"]!!.toInt()
                    val request = GetSubmissionDetailsRequest(id)
                    val response = interactor.handleRequest(request) as GetSubmissionDetailsResponse
                    call.respondHtml { makeSubmissionDetailsPage(response) }
                }
            }
        }
    }
}
