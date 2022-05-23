package ru.hse.sd.hwproj

import ru.hse.sd.hwproj.interactor.EntityGateway
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.server.createServer
import ru.hse.sd.hwproj.storage.sqlite.SQLiteStorage

fun main() {
    val storage = SQLiteStorage()

    val entityGateway = EntityGateway(storage)
    val interactor = Interactor(entityGateway)

    createServer(interactor).start(wait = true)
}