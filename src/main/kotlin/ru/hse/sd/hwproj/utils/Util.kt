package ru.hse.sd.hwproj.utils

import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

inline fun <reified E : Throwable> StatusPagesConfig.wrapper(code: HttpStatusCode) =
    exception<E> { call, cause -> call.respond(code, cause.message ?: "") }
