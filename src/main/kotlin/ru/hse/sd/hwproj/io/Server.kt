package ru.hse.sd.hwproj.io

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.statuspages.*
import kotlinx.serialization.SerializationException
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.slf4j.event.Level
import ru.hse.sd.hwproj.exceptions.NoSuchAssignment
import ru.hse.sd.hwproj.exceptions.NoSuchSubmission
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.io.html.addLandingHTMLModule
import ru.hse.sd.hwproj.io.html.student.addStudentHTMLModule
import ru.hse.sd.hwproj.io.html.teacher.addTeacherHTMLModule
import ru.hse.sd.hwproj.io.rest.addRESTModule
import ru.hse.sd.hwproj.utils.wrapper

/**
 * Creates a new Ktor [Application] for the server.
 *
 * This server can respond with REST HTTP on /api endpoint and with HTML pages on / endpoint.
 *
 * @param interactor The [Interactor] to handle user requests.
 */
fun createServer(interactor: Interactor) = embeddedServer(Netty, port = 8080) {
    install(CallLogging) {
        level = Level.INFO
    }

    installStatusPages()

    addRESTModule(interactor)
    addStudentHTMLModule(interactor)
    addTeacherHTMLModule(interactor)
    addLandingHTMLModule()
}

fun Application.installStatusPages() {
    install(StatusPages) {
        wrapper<NoSuchAssignment>(HttpStatusCode.NotFound)
        wrapper<NoSuchSubmission>(HttpStatusCode.NotFound)
        wrapper<NumberFormatException>(HttpStatusCode.BadRequest)
        wrapper<EntityNotFoundException>(HttpStatusCode.BadRequest)
        wrapper<SerializationException>(HttpStatusCode.BadRequest)
    }
}
