package ru.hse.sd.hwproj.messagebroker

interface RunnerMessageBroker {

    fun handleCheckTasks(check: (SubmissionCheckTask) -> SubmissionCheckResult)

}
