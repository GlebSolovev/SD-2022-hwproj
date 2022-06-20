package ru.hse.sd.hwproj.messagebroker

import ru.hse.sd.hwproj.runner.Runner

/**
 * [Runner]-side interface for receiving tasks for checking from entities and sending [SubmissionCheckStatus]-es back.
 */
interface RunnerMessageBroker {

    /**
     * Infinitely receives [SubmissionCheckTask]-s one after another and handles them with [check] callback.
     * Then the result of [check] is sent back to the task sender.
     */
    fun handleCheckTasks(check: (SubmissionCheckTask) -> SubmissionCheckStatus)

}
