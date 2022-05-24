package ru.hse.sd.hwproj.server.html.teacher

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.server.html.makeListSubmissionsPage
import ru.hse.sd.hwproj.server.html.makeSubmissionDetailsPage
import ru.hse.sd.hwproj.utils.CheckerProgram
import ru.hse.sd.hwproj.utils.parseHTMLTimestamp

fun Application.addTeacherHTMLModule(interactor: Interactor) {
    routing {
        route("/teacher") {
            get("/assignments") {
                val request = ListAssignmentsRequest()
                val response = interactor.handleRequest(request) as ListAssignmentsResponse
                call.respondHtml { makeTeacherAssignmentsPage(response) }
            }
            route("/assignments/new") {
                get {
                    call.respondHtml { makeTeacherNewAssignmentPage() }
                }
                post {
                    val formParameters = call.receiveParameters()
                    val name = formParameters["name"]!!
                    val taskText = formParameters["taskText"]!!
                    val publicationTimestamp = parseHTMLTimestamp(formParameters["publicationTime"]!!)
                    val deadlineTimestamp = parseHTMLTimestamp(formParameters["deadlineTime"]!!)
                    val checker = formParameters["checker"]?.toByteArray()

                    val request = CreateAssignmentRequest(
                        name, taskText, publicationTimestamp, deadlineTimestamp, checker?.let { CheckerProgram(it) }
                    )
                    val response = interactor.handleRequest(request) as CreateAssignmentResponse
                    call.respondHtml { makeTeacherCreatedAssignmentPage(response) }
                }
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
