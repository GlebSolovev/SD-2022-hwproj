package ru.hse.sd.hwproj.messagebroker.rabbtimq

import com.rabbitmq.client.MessageProperties
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.hse.sd.hwproj.messagebroker.EntityMessageBroker
import ru.hse.sd.hwproj.messagebroker.SubmissionCheckResult
import ru.hse.sd.hwproj.messagebroker.SubmissionCheckTask

class RabbitMQEntityMessageBroker(
    serverHost: String = "localhost",
    taskQueueName: String = "task_queue"
) : AbstractRabbitMQMessageBroker(serverHost, taskQueueName), EntityMessageBroker {

    override fun sendCheckTask(task: SubmissionCheckTask, onReady: (SubmissionCheckResult) -> Unit) {
        withConnection(connection) {
            channel.basicPublish(
                "",
                taskQueueName,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                Json.encodeToString(task).toByteArray(charset)
            )
        }
    }
}
