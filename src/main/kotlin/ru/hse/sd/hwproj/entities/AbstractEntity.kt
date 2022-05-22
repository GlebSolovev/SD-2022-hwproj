package ru.hse.sd.hwproj.entities

import ru.hse.sd.hwproj.models.RequestModel
import ru.hse.sd.hwproj.models.ResponseModel

abstract class AbstractEntity<R: RequestModel> {

    abstract fun execute(request: R): ResponseModel

}
