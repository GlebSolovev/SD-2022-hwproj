package ru.hse.sd.hwproj.storage.sqlite

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.entity.Entity
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.ktorm.schema.*
import ru.hse.sd.hwproj.storage.AssignmentORM
import ru.hse.sd.hwproj.storage.CheckResultORM
import ru.hse.sd.hwproj.storage.Storage
import ru.hse.sd.hwproj.storage.SubmissionORM
import ru.hse.sd.hwproj.utils.CheckerProgram
import ru.hse.sd.hwproj.utils.Timestamp
import java.time.Instant

class SQLiteStorage : Storage {

    private val database: Database

    init {
        val createAssignmentsTable = """
            CREATE TABLE IF NOT EXISTS t_assignment (
            name TEXT NOT NULL,
            taskText TEXT NOT NULL,
            publicationTimestamp TIMESTAMP NOT NULL,
            deadlineTimestamp TIMESTAMP NOT NULL,
            checkerProgram BYTES,
            id INT PRIMARY KEY
            );
        """.trimIndent()
        val createSubmissionsTable = """
            CREATE TABLE IF NOT EXISTS t_submission (
            submissionTimestamp TIMESTAMP NOT NULL,
            submissionLink TEXT NOT NULL,
            assignmentId INT REFERENCES t_assignment (id) NOT NULL,
            checkResultId INT REFERENCES t_check_result (id) NOT NULL,
            id INT PRIMARY KEY
            );
        """.trimIndent()
        val createCheckResultsTable = """
            CREATE TABLE IF NOT EXISTS t_check_result (
            success BOOLEAN NOT NULL,
            output TEXT NOT NULL,
            id INT PRIMARY KEY
            );
        """.trimIndent()

        database = Database.connect("jdbc:sqlite:temp/test.db")

        database.useConnection { connection ->
            connection.createStatement().use {
                it.execute(createAssignmentsTable)
                it.execute(createCheckResultsTable)
                it.execute(createSubmissionsTable)
            }
        }
    }

    object Assignments : Table<Assignment>("t_assignment") {
        val name = text("name").bindTo { it.name }
        val taskText = text("taskText").bindTo { it.taskText }
        val publicationTimestamp = timestamp("publicationTimestamp").bindTo { it.publicationTimestamp }
        val deadlineTimestamp = timestamp("deadlineTimestamp").bindTo { it.deadlineTimestamp }
        val checkerProgram = bytes("checkerProgram").bindTo { it.checkerProgram }

        val id = int("id").primaryKey().bindTo { it.id }
    }

    object Submissions : Table<Submission>("t_submission") {
        val submissionTimestamp = timestamp("submissionTimestamp").bindTo { it.submissionTimestamp }
        val submissionLink = text("submissionLink").bindTo { it.submissionLink }

        val assignmentId = int("assignmentId").references(Assignments) { it.assignment }
        val checkResultId = int("checkResultId").references(CheckResults) { it.checkResult }

        val id = int("id").primaryKey().bindTo { it.id }
    }

    object CheckResults : Table<CheckResult>("t_check_result") {
        val success = boolean("success").bindTo { it.success }
        val output = text("output").bindTo { it.output }

        val id = int("id").primaryKey().bindTo { it.id }
    }

    interface Assignment : AssignmentORM, Entity<Assignment>

    interface Submission : SubmissionORM, Entity<Submission> {
        override val assignment: Assignment
        override val checkResult: CheckResult
    }

    interface CheckResult : CheckResultORM, Entity<CheckResult>

    override fun listSubmissions(): List<SubmissionORM> = database.sequenceOf(Submissions).toList()

    override fun listAssignments(): List<AssignmentORM> = database.sequenceOf(Assignments).toList()

    override fun getSubmission(id: Int): SubmissionORM? = database.sequenceOf(Submissions).firstOrNull { it.id eq id }

    override fun createSubmission(assignmentId: Int, submissionLink: String): Int =
        database.insertAndGenerateKey(Submissions) {
            set(it.submissionTimestamp, Instant.now())
            set(it.submissionLink, submissionLink)

            set(it.assignmentId, assignmentId)
            set(it.checkResultId, null)
        } as Int
    // TODO: error handling ? (foreign key)
    // TODO: constraints - in tables?

    override fun createAssignment(
        name: String,
        taskText: String,
        publicationTimestamp: Timestamp,
        deadlineTimestamp: Timestamp,
        checker: CheckerProgram?
    ): Int = database.insertAndGenerateKey(Assignments) {
        set(it.name, name)
        set(it.taskText, taskText)
        set(it.publicationTimestamp, publicationTimestamp)
        set(it.deadlineTimestamp, deadlineTimestamp)
        set(it.checkerProgram, checker?.bytes)
    } as Int
    // TODO: constraints

}