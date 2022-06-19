package ru.hse.sd.hwproj.entities

import ru.hse.sd.hwproj.models.RequestModel
import ru.hse.sd.hwproj.models.ResponseModel

/**
 * Base class for handling one user request.
 */
sealed class AbstractEntity<R : RequestModel> {

    /**
     * Executes user [request] and returns the corresponding [RequestModel].
     */
    abstract fun execute(request: R): ResponseModel
}
