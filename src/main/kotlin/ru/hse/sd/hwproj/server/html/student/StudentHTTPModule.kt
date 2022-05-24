package ru.hse.sd.hwproj.server.html.student

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.models.ListAssignmentsRequest
import ru.hse.sd.hwproj.models.ListAssignmentsResponse
import ru.hse.sd.hwproj.server.html.formListAssignmentsPage

fun Application.addStudentHTMLModule(interactor: Interactor) {
    routing {
        route("/student") {
            get("/assignments") {
                val request = ListAssignmentsRequest()
                val response = interactor.handleRequest(request) as ListAssignmentsResponse
                call.respondHtml { formListAssignmentsPage(response) }
            }
        }
    }
}