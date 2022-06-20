package ru.hse.sd.hwproj.runner

import ru.hse.sd.hwproj.messagebroker.RunnerMessageBroker
import ru.hse.sd.hwproj.messagebroker.SubmissionCheckResult
import ru.hse.sd.hwproj.messagebroker.rabbtimq.RabbitMQRunnerMessageBroker

fun main() {
    val messageBroker: RunnerMessageBroker = RabbitMQRunnerMessageBroker()

    messageBroker.handleCheckTasks { checkTask ->
        println("BEGIN CHECKING $checkTask")
        return@handleCheckTasks SubmissionCheckResult(true, "DUMMY PASSED")
    }
}
