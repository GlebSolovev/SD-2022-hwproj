package ru.hse.sd.hwproj.io.html

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Adds a Ktor [Application] module with HTML landing pages.
 */
fun Application.addLandingHTMLModule() {
    routing {
        get {
            call.respondHtml { makeWelcomePage() }
        }
        get("/student") {
            call.respondRedirect("/student/assignments")
        }
        get("/teacher") {
            call.respondRedirect("/teacher/assignments")
        }
    }
}
