package ru.hse.sd.hwproj.messagebroker.rabbtimq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import java.nio.charset.Charset

abstract class AbstractRabbitMQMessageBroker(serverHost: String, protected val taskQueueName: String) {

    private val connectionFactory = ConnectionFactory().also { it.host = serverHost }
    protected val connection: Connection = connectionFactory.newConnection()
    protected lateinit var channel: Channel

    protected val charset: Charset = Charset.forName("UTF-8")

    init {
        withConnection(connection) {
            channel = connection.createChannel()
            channel.queueDeclare(taskQueueName, true, false, false, null)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    protected inline fun <R> withConnection(connection: Connection, crossinline block: () -> R): R {
        try {
            return block()
        } catch (exception: Exception) {
            connection.close()
            throw exception
        }
    }

}
