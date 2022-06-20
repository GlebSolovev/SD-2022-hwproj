package ru.hse.sd.hwproj

import ru.hse.sd.hwproj.interactor.EntityGateway
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.io.createServer
import ru.hse.sd.hwproj.messagebroker.rabbtimq.RabbitMQEntityMessageBroker
import ru.hse.sd.hwproj.storage.sqlite.SQLiteStorage
import java.io.File
import java.nio.file.Paths

private const val STORAGE_DIRECTORY_PATH = "src/main/resources/db"
private const val STORAGE_FILE = "test.db"

/**
 * Application entry point.
 */
fun main() {
    File(STORAGE_DIRECTORY_PATH).mkdirs()
    val storage = SQLiteStorage(Paths.get(STORAGE_DIRECTORY_PATH, STORAGE_FILE).toString())

    val messageBroker = RabbitMQEntityMessageBroker()

    val entityGateway = EntityGateway(storage, messageBroker)
    val interactor = Interactor(entityGateway)

    createServer(interactor).start(wait = true)
}
