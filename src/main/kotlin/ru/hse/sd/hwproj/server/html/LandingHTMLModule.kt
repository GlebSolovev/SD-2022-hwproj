package ru.hse.sd.hwproj.server.html

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import ru.hse.sd.hwproj.server.html.student.makeStudentLandingPage
import ru.hse.sd.hwproj.server.html.teacher.makeTeacherLandingPage

fun Application.addLandingHTMLModule() {
    routing {
        get {
            call.respondHtml { makeWelcomePage() }
        }
        get("/student") {
            call.respondHtml { makeStudentLandingPage() }
        }
        get("/teacher") {
            call.respondHtml { makeTeacherLandingPage() }
        }
    }
}
