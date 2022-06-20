package ru.hse.sd.hwproj.exceptions

/**
 * Self-explanatory.
 */
class NoSuchAssignment(id: Int) : Exception("no assignment with id $id")

/**
 * Self-explanatory.
 */
class NoSuchSubmission(id: Int) : Exception("no submission with id $id")

/**
 * Self-explanatory.
 */
class InvalidHttpForm(reason: String) : Exception(reason)
