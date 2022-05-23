package ru.hse.sd.hwproj.storage.sqlite

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import org.sqlite.SQLiteDataSource
import ru.hse.sd.hwproj.exceptions.NoSuchSubmission
import ru.hse.sd.hwproj.storage.AssignmentORM
import ru.hse.sd.hwproj.storage.CheckResultORM
import ru.hse.sd.hwproj.storage.Storage
import ru.hse.sd.hwproj.storage.SubmissionORM
import ru.hse.sd.hwproj.utils.CheckerProgram
import ru.hse.sd.hwproj.utils.Timestamp
import java.time.Instant

// if no source provided, use in-memory DB
class SQLiteStorage(sourcePath: String?) : Storage {

    object Assignments : IntIdTable() {
        val name = text("name")
        val taskText = text("taskText")
        val publicationTimestamp = timestamp("publicationTimestamp")
        val deadlineTimestamp = timestamp("deadlineTimestamp")
        val checkerProgram = binary("checkerProgram").nullable()
    }

    class Assignment(id: EntityID<Int>) : IntEntity(id), AssignmentORM {
        companion object : IntEntityClass<Assignment>(Assignments)

        override var name by Assignments.name
        override var taskText by Assignments.taskText
        override var publicationTimestamp by Assignments.publicationTimestamp
        override var deadlineTimestamp by Assignments.deadlineTimestamp
        override var checkerProgram by Assignments.checkerProgram

        override val _id by id::value
    }

    object Submissions : IntIdTable() {
        val submissionTimestamp = timestamp("submissionTimestamp")
        val submissionLink = text("submissionLink")

        val assignmentId = reference("assignmentId", Assignments)
        val checkResultId = reference("checkResultId", CheckResults).nullable()
    }

    object CheckResults : IntIdTable() {
        val success = bool("success")
        val output = text("output")
    }

    class CheckResult(id: EntityID<Int>) : IntEntity(id), CheckResultORM {
        companion object : IntEntityClass<CheckResult>(CheckResults)

        override var success by CheckResults.success
        override var output by CheckResults.output

        override val _id by id::value
    }

    class Submission(id: EntityID<Int>) : IntEntity(id), SubmissionORM {
        companion object : IntEntityClass<Submission>(Submissions)

        override var submissionTimestamp by Submissions.submissionTimestamp
        override var submissionLink by Submissions.submissionLink

        override var assignment by Assignment referencedOn Submissions.assignmentId
        override var checkResult by CheckResult optionalReferencedOn Submissions.checkResultId

        override val _id by id::value
    }

    init {
        val url = if (sourcePath != null)
            "jdbc:sqlite:$sourcePath"
        else
            "jdbc:sqlite:file:test?mode=memory&cache=shared"

        val source = SQLiteDataSource().apply {
            this.url = url
        }

        Database.connect(source)

        transaction {
            SchemaUtils.create(Assignments, CheckResults, Submissions)
        }
    }

    // ------------ interface implementation ------------

    // accessing Assignment outside of transaction throws
    // TODO: avoid this somehow?..
    private fun submissionConverter(s: Submission) = object : SubmissionORM by s {
        override val assignment = s.assignment
        override val checkResult = s.checkResult
    }

    override fun listSubmissions(): List<SubmissionORM> = transaction {
        Submission
            .all()
            .sortedBy { it.submissionTimestamp }
            .map(::submissionConverter)
    }

    override fun listAssignments(): List<AssignmentORM> = transaction {
        Assignment
            .find { Assignments.publicationTimestamp less Timestamp.now() }
            .sortedBy { it.deadlineTimestamp }
    }

    override fun getSubmission(id: Int): SubmissionORM = transaction {
        Submission
            .findById(id)
            ?.let { submissionConverter(it) }
    } ?: throw NoSuchSubmission(id)

    override fun createSubmission(assignmentId: Int, submissionLink: String): Int = transaction {
        Submission.new {
            this.submissionTimestamp = Instant.now()
            this.submissionLink = submissionLink

            this.assignment = Assignment[assignmentId]
            this.checkResult = null
        }._id
    }
    // TODO: error handling ? (foreign key) ===> throws EntityNotFoundException
    // TODO: constraints - in tables? ===> maybe not, let checkerProgram handle it

    override fun createAssignment(
        name: String,
        taskText: String,
        publicationTimestamp: Timestamp,
        deadlineTimestamp: Timestamp,
        checker: CheckerProgram?
    ): Int = transaction {
        Assignment.new {
            this.name = name
            this.taskText = taskText
            this.publicationTimestamp = publicationTimestamp
            this.deadlineTimestamp = deadlineTimestamp
            this.checkerProgram = checker?.bytes
        }._id
    }

}