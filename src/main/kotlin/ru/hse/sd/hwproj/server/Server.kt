package ru.hse.sd.hwproj.server

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import org.slf4j.event.Level
import ru.hse.sd.hwproj.interactor.Interactor

fun createServer(interactor: Interactor) = embeddedServer(Netty, port = 8080) {
    install(CallLogging) {
        level = Level.INFO
    }

    createRESTModule(interactor)
}
