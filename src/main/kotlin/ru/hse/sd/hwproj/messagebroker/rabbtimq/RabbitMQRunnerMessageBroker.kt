package ru.hse.sd.hwproj.messagebroker.rabbtimq

import com.rabbitmq.client.Delivery
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.hse.sd.hwproj.messagebroker.CheckStatus
import ru.hse.sd.hwproj.messagebroker.RunnerMessageBroker
import ru.hse.sd.hwproj.messagebroker.SubmissionCheckStatus
import ru.hse.sd.hwproj.messagebroker.SubmissionCheckTask
import ru.hse.sd.hwproj.runner.Runner

/**
 * [Runner]-side RabbitMQ client that implements [RunnerMessageBroker].
 *
 * Consumes check tasks directly from special queue. Sends statuses of check tasks via another queue.
 *
 * @param serverHost The host name of RabbitMQ server.
 * @param taskQueueName The name of the queue for receiving check tasks.
 * @param statusQueueName The name of the queue for sending statuses of check tasks.
 */
class RabbitMQRunnerMessageBroker(
    serverHost: String = "rabbitmq",
    taskQueueName: String = "task_queue",
    statusQueueName: String = "status_queue"
) : AbstractRabbitMQMessageBroker(serverHost, taskQueueName, statusQueueName), RunnerMessageBroker {

    /**
     * Implements [RunnerMessageBroker.handleCheckTasks].
     */
    @Suppress("TooGenericExceptionCaught")
    override fun handleCheckTasks(check: (SubmissionCheckTask) -> SubmissionCheckStatus) {
        withConnection(connection) {
            taskChannel.basicQos(1)

            val deliverCallback = { _: String, delivery: Delivery ->
                val task = Json.decodeFromString<SubmissionCheckTask>(delivery.body.toString(charset))
                try {
                    sendCheckStatus(SubmissionCheckStatus(task.submissionId, CheckStatus.IN_PROGRESS, null))
                    val checkResult = check(task)
                    sendCheckStatus(checkResult)
                } catch (exception: Exception) {
                    sendCheckStatus(SubmissionCheckStatus(task.submissionId, CheckStatus.ERROR, null))
                    throw exception
                } finally {
                    taskChannel.basicAck(delivery.envelope.deliveryTag, false)
                }
            }

            taskChannel.basicConsume(taskQueueName, false, deliverCallback) { _ -> }
        }
    }

    private fun sendCheckStatus(status: SubmissionCheckStatus) {
        withConnection(connection) {
            statusChannel.basicPublish("", statusQueueName, null, Json.encodeToString(status).toByteArray(charset))
        }
    }

}
