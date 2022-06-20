package ru.hse.sd.hwproj.messagebroker.rabbtimq

import com.rabbitmq.client.Delivery
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.hse.sd.hwproj.messagebroker.RunnerMessageBroker
import ru.hse.sd.hwproj.messagebroker.SubmissionCheckResult
import ru.hse.sd.hwproj.messagebroker.SubmissionCheckTask

class RabbitMQRunnerMessageBroker(
    serverHost: String = "localhost",
    taskQueueName: String = "task_queue"
) : AbstractRabbitMQMessageBroker(serverHost, taskQueueName), RunnerMessageBroker {

    override fun handleCheckTasks(check: (SubmissionCheckTask) -> SubmissionCheckResult) {
        withConnection(connection) {
            channel.basicQos(1)

            val deliverCallback = { _: String, delivery: Delivery ->
                val task = Json.decodeFromString<SubmissionCheckTask>(delivery.body.toString(charset))
                try {
                    val checkResult = check(task)
                    println("CHECKED: $checkResult")
                } finally {
                    channel.basicAck(delivery.envelope.deliveryTag, false)
                }
            }

            channel.basicConsume(taskQueueName, false, deliverCallback) { _ -> }
        }
    }
}
