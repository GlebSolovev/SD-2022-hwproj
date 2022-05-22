package ru.hse.sd.hwproj.interactor

import ru.hse.sd.hwproj.storage.Storage

class EntityGateway(private val storage: Storage) {

    inline fun <reified E> getEntity(): E {
        TODO("Not yet implemented")
    }

}