package ru.hse.sd.hwproj.io.html.teacher

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import ru.hse.sd.hwproj.exceptions.InvalidHttpForm
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.utils.CheckerProgram
import ru.hse.sd.hwproj.utils.parseHTMLTimestamp

/**
 * Adds a Ktor [Application] module with HTML pages for teachers.
 */
fun Application.addTeacherHTMLModule(interactor: Interactor) {
    routing {
        route("/teacher") {
            get("/assignments") {
                val request = ListAssignmentsRequest()
                val response = interactor.handleRequest(request) as ListAssignmentsResponse
                call.respondHtml { makeTeacherAssignmentsPage(response) }
            }
            get("/assignments/{id}") {
                val id = call.parameters["id"]!!.toInt()
                val request = GetAssignmentDetailsRequest(id)
                val response = interactor.handleRequest(request) as GetAssignmentDetailsResponse
                call.respondHtml { makeTeacherAssignmentDetailsPage(response) }
            }
            route("/assignments/new") {
                get {
                    call.respondHtml { makeTeacherNewAssignmentPage() }
                }
                post {
                    val parts = call.receiveMultipart().readAllParts()
                    val formParts = parts
                        .filterIsInstance<PartData.FormItem>()
                        .associate { Pair(it.name, it.value) }
                    val name = formParts["name"]!!
                    val taskText = formParts["taskText"]!!
                    val publicationTimestamp = parseHTMLTimestamp(formParts["publicationTime"]!!)
                    val deadlineTimestamp = parseHTMLTimestamp(formParts["deadlineTime"]!!)

                    val fileParts = parts.filterIsInstance<PartData.FileItem>()
                    val checker = if (fileParts.isNotEmpty()) {
                        if (fileParts.size != 1) throw InvalidHttpForm("expected 1 file part for checker")
                        fileParts[0].streamProvider().readBytes()
                    } else null

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
                call.respondHtml { makeTeacherSubmissionListPage(response) }
            }
            get("/submissions/{id}") {
                val id = call.parameters["id"]!!.toInt()
                val request = GetSubmissionDetailsRequest(id)
                val response = interactor.handleRequest(request) as GetSubmissionDetailsResponse
                call.respondHtml { makeTeacherSubmissionDetailsPage(response) }
            }
        }
    }
}
