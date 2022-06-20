package ru.hse.sd.hwproj.messagebroker.rabbtimq

import com.rabbitmq.client.Delivery
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.hse.sd.hwproj.messagebroker.EntityMessageBroker
import ru.hse.sd.hwproj.messagebroker.SubmissionCheckStatus
import ru.hse.sd.hwproj.messagebroker.SubmissionCheckTask
import ru.hse.sd.hwproj.runner.Runner
import java.util.concurrent.ConcurrentHashMap

/**
 * Entities-side RabbitMQ client that implements [EntityMessageBroker].
 *
 * Sends check tasks to [Runner]-s directly via special queue. Receives statuses of check tasks in a separate thread via
 * another queue and calls saved callbacks (obtained in [RabbitMQEntityMessageBroker.sendCheckTask]) on them.
 *
 * @param serverHost The host name of RabbitMQ server.
 * @param taskQueueName The name of the queue for sending check tasks.
 * @param statusQueueName The name of the queue for receiving statuses of check tasks.
 */
class RabbitMQEntityMessageBroker(
    serverHost: String = "localhost",
    taskQueueName: String = "task_queue",
    statusQueueName: String = "status_queue"
) : AbstractRabbitMQMessageBroker(serverHost, taskQueueName, statusQueueName), EntityMessageBroker {

    private val onCheckStatusReadyCallbacks = ConcurrentHashMap<Int, (SubmissionCheckStatus) -> Unit>()

    @OptIn(DelicateCoroutinesApi::class)
    private val handleCheckStatusesContext = newSingleThreadContext("check statuses")

    init {
        handleCheckStatusesContext.use { ctx ->
            runBlocking(ctx) {
                handleCheckStatuses()
            }
        }
    }

    /**
     * Implements [EntityMessageBroker.sendCheckTask].
     */
    override fun sendCheckTask(task: SubmissionCheckTask, onCheckStatusReady: (SubmissionCheckStatus) -> Unit) {
        onCheckStatusReadyCallbacks[task.submissionId] = onCheckStatusReady
        withConnection(connection) {
            taskChannel.basicPublish(
                "",
                taskQueueName,
                null,
                Json.encodeToString(task).toByteArray(charset)
            )
        }
    }

    private fun handleCheckStatuses() {
        withConnection(connection) {
            taskChannel.basicQos(0)

            val deliverCallback = { _: String, delivery: Delivery ->
                val checkStatus = Json.decodeFromString<SubmissionCheckStatus>(delivery.body.toString(charset))
                try {
                    onCheckStatusReadyCallbacks[checkStatus.submissionId]!!(checkStatus)
                } finally {
                    statusChannel.basicAck(delivery.envelope.deliveryTag, false)
                }
            }
            statusChannel.basicConsume(statusQueueName, false, deliverCallback) { _ -> }
        }
    }

}
