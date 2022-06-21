package ru.hse.sd.hwproj.io.rest

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.models.*

/**
 * Adds a Ktor [Application] module for handling REST HTTP requests.
 */
fun Application.addRESTModule(interactor: Interactor) {
    routing {
        route("/api") {
            install(ContentNegotiation) {
                json()
            }
            routeREST(interactor)
        }
    }
}

private fun Route.routeREST(interactor: Interactor) {
    route("/assignments") {
        get {
            val showUnpublished = call.request.queryParameters["showUnpublished"] == "true"
            call.respond(interactor.handleRequest(ListAssignmentsRequest(showUnpublished)) as ListAssignmentsResponse)
        }
        post {
            val request = call.receive<CreateAssignmentRequest>()
            call.respond(interactor.handleRequest(request) as CreateAssignmentResponse)
        }
        get("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            call.respond(interactor.handleRequest(GetAssignmentDetailsRequest(id)) as GetAssignmentDetailsResponse)
        }
    }

    route("/submissions") {
        get {
            call.respond(interactor.handleRequest(ListSubmissionsRequest()) as ListSubmissionsResponse)
        }
        post {
            val request = call.receive<SubmitSubmissionRequest>()
            call.respond(interactor.handleRequest(request) as SubmitSubmissionResponse)
        }
        get("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            call.respond(interactor.handleRequest(GetSubmissionDetailsRequest(id)) as GetSubmissionDetailsResponse)
        }
    }
}
