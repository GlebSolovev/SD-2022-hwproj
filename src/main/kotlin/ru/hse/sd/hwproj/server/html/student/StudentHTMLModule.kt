package ru.hse.sd.hwproj.server.html.student

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.server.html.makeListSubmissionsPage
import ru.hse.sd.hwproj.server.html.makeSubmissionDetailsPage

fun Application.addStudentHTMLModule(interactor: Interactor) {
    routing {
        route("/student") {
            get("/assignments") {
                val request = ListAssignmentsRequest()
                val response = interactor.handleRequest(request) as ListAssignmentsResponse
                call.respondHtml { makeStudentAssignmentsPage(response) }
            }
            get("/submissions") {
                val request = ListSubmissionsRequest()
                val response = interactor.handleRequest(request) as ListSubmissionsResponse
                call.respondHtml { makeListSubmissionsPage(response) }
            }
            get("/submissions/{id}") {
                val id = call.parameters["id"]!!.toInt()
                val request = GetSubmissionDetailsRequest(id)
                val response = interactor.handleRequest(request) as GetSubmissionDetailsResponse
                call.respondHtml { makeSubmissionDetailsPage(response) }
            }
        }
    }
}