package ru.hse.sd.hwproj.runner

import ru.hse.sd.hwproj.messagebroker.CheckStatus
import ru.hse.sd.hwproj.messagebroker.RunnerMessageBroker
import ru.hse.sd.hwproj.messagebroker.SubmissionCheckStatus
import ru.hse.sd.hwproj.messagebroker.SubmissionCheckTask
import java.io.File
import java.nio.file.Files
import java.util.concurrent.TimeUnit

/**
 * Class for receiving and executing check tasks.
 *
 * @property messageBroker The [RunnerMessageBroker] that will be used to receive check tasks and send back their
 * results.
 * @property workingDirectory The directory to execute checker scripts in.
 * @property bashRunTimeoutMinutes The timeout in minutes to run one checker script.
 */
class Runner(
    private val messageBroker: RunnerMessageBroker,
    private val workingDirectory: String,
    private val bashRunTimeoutMinutes: Long = 1L
) {

    companion object {
        private const val DEFAULT_SCRIPT_FILENAME = "test-script"
    }

    /**
     * Infinitely receives [SubmissionCheckTask]-s from [messageBroker] and performs them, sending back the results.
     * Perform a task = execute task's checker bash script and send back [SubmissionCheckStatus] corresponding to the
     * exit code of the script run.
     */
    @Suppress("TooGenericExceptionCaught")
    fun receiveAndCheckTasks() {
        messageBroker.handleCheckTasks { checkTask ->
            val scriptName = DEFAULT_SCRIPT_FILENAME + "-${checkTask.submissionId}.sh"
            val scriptFile = File(workingDirectory).resolve(scriptName).absoluteFile
            try {
                scriptFile.writeBytes(checkTask.checker.bytes)

                val runResult = runBashScript(scriptName, checkTask.submissionLink)
                val status = if (runResult.exitCode == 0) CheckStatus.OK else CheckStatus.FAILED
                return@handleCheckTasks SubmissionCheckStatus(checkTask.submissionId, status, runResult.outputString)

            } catch (exception: Exception) {
                println("Exception occurred while checking submission #${checkTask.submissionId}:\n$exception")
                exception.printStackTrace()
                return@handleCheckTasks SubmissionCheckStatus(checkTask.submissionId, CheckStatus.ERROR, null)
            } finally {
                Files.deleteIfExists(scriptFile.toPath())
            }
        }
    }

    private data class BashRunResult(val exitCode: Int, val outputString: String)

    private fun runBashScript(bashScriptFilePath: String, submissionLink: String): BashRunResult {
        val proc = ProcessBuilder("bash", bashScriptFilePath, submissionLink)
            .directory(File(workingDirectory).absoluteFile)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        proc.waitFor(bashRunTimeoutMinutes, TimeUnit.MINUTES)

        val exitCode = proc.exitValue()
        val outputString = proc.inputStream.bufferedReader().readText()
        return BashRunResult(exitCode, outputString)
    }

}
