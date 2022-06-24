package ru.hse.sd.hwproj.messagebroker

import ru.hse.sd.hwproj.runner.Runner

/**
 * Entities-side interface for sending tasks for checking to [Runner]-s.
 */
interface EntityMessageBroker {

    /**
     * Sends [task] for [SubmissionCheckTask.checker] execution, possibly on another machine.
     * When [EntityMessageBroker] receives [SubmissionCheckStatus] of that check, [onCheckStatusReady] is called.
     */
    fun sendCheckTask(task: SubmissionCheckTask, onCheckStatusReady: (SubmissionCheckStatus) -> Unit)

}
