package ru.hse.sd.hwproj.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.statuspages.*
import kotlinx.serialization.SerializationException
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.slf4j.event.Level
import ru.hse.sd.hwproj.exceptions.InvalidFormException
import ru.hse.sd.hwproj.exceptions.NoSuchAssignment
import ru.hse.sd.hwproj.exceptions.NoSuchSubmission
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.server.html.addLandingHTMLModule
import ru.hse.sd.hwproj.server.html.student.addStudentHTMLModule
import ru.hse.sd.hwproj.server.html.teacher.addTeacherHTMLModule
import ru.hse.sd.hwproj.utils.wrapper

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
        wrapper<InvalidFormException>(HttpStatusCode.BadRequest)
    }
}
