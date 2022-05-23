package ru.hse.sd.hwproj.server

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.hse.sd.hwproj.exceptions.NoSuchAssignment
import ru.hse.sd.hwproj.exceptions.NoSuchSubmission
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.utils.wrapper

fun Application.createRESTModule(interactor: Interactor) {
    install(ContentNegotiation) {
        json()
    }

    routing {
        routeREST(interactor)
    }

    install(StatusPages) {
        wrapper<NoSuchAssignment>(HttpStatusCode.NotFound)
        wrapper<NoSuchSubmission>(HttpStatusCode.NotFound)
        wrapper<NumberFormatException>(HttpStatusCode.BadRequest)
    }
}

fun Route.routeREST(interactor: Interactor) {
    route("/api") {

        route("/assignments") {
            get {
                call.respond(interactor.handleRequest(ListAssignmentsRequest()) as ListAssignmentsResponse)
            }
            post {
                val request = call.receive<CreateAssignmentRequest>()
                call.respond(interactor.handleRequest(request) as CreateAssignmentResponse)
            }
        }

        route("/submissions") {
            get {
                call.respond(interactor.handleRequest(ListSubmissionsRequest()) as ListAssignmentsResponse)
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
}
