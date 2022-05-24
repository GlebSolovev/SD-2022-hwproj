package ru.hse.sd.hwproj.exceptions

class NoSuchAssignment(id: Int) : Exception("no assignment with id $id")

class NoSuchSubmission(id: Int) : Exception("no submission with id $id")

class InvalidFormException : Exception()
