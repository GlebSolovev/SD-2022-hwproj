package ru.hse.sd.hwproj.runner

import ru.hse.sd.hwproj.messagebroker.rabbtimq.RabbitMQRunnerMessageBroker
import java.io.File

private const val RUNNER_DIRECTORY_PATH = "src/main/resources/runner-workspace"

/**
 * Runner application entry point.
 */
fun main() {
    File(RUNNER_DIRECTORY_PATH).mkdirs()
    val messageBroker = RabbitMQRunnerMessageBroker()

    val runner = Runner(messageBroker, RUNNER_DIRECTORY_PATH)
    runner.receiveAndCheckTasks()
}
