package ru.hse.sd.hwproj.messagebroker

interface EntityMessageBroker {

    fun sendCheckTask(task: SubmissionCheckTask, onReady: (SubmissionCheckResult) -> Unit)

}
