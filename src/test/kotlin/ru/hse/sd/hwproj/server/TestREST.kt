@file:UseSerializers(TimestampSerializer::class)

package ru.hse.sd.hwproj.server

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.coroutines.delay
import kotlinx.serialization.UseSerializers
import ru.hse.sd.hwproj.interactor.EntityGateway
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.storage.sqlite.SQLiteStorage
import ru.hse.sd.hwproj.utils.CheckerProgram
import ru.hse.sd.hwproj.utils.Timestamp
import ru.hse.sd.hwproj.utils.TimestampSerializer
import kotlin.io.path.createTempDirectory
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestREST {

    private val tempDir = createTempDirectory("hwproj-rest-test")
    private lateinit var storage: SQLiteStorage

    private val sampleName = "Sample Name"
    private val sampleDescription = "sample description"
    private val sampleFutureTimestamp = Timestamp.parse("2036-05-23T23:05:00.00Z")
    private val sampleOldTimestamp = Timestamp.parse("2007-12-03T10:15:30.00Z")

    private val sampleChecker = CheckerProgram(byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7))

    @BeforeTest
    fun generateNewSQLiteStorage() {
        storage = SQLiteStorage(kotlin.io.path.createTempFile(tempDir).toAbsolutePath().toString())
    }

    private fun ApplicationTestBuilder.doSetup(): HttpClient {
        application {
            installStatusPages()
            addRESTModule(Interactor(EntityGateway(storage)))
        }
        return createClient {
            this@createClient.install(ContentNegotiation) {
                json()
            }
        }
    }

    @Test
    fun testAssignments() = testApplication {
        val client = doSetup()

        val resp1 = client.get("/api/assignments")
        val respModel1 = resp1.call.body<ListAssignmentsResponse>()
        assertEquals(emptyList(), respModel1.assignments)

        val reqModel2 = CreateAssignmentRequest(
            sampleName,
            sampleDescription,
            sampleOldTimestamp,
            sampleOldTimestamp,
            sampleChecker
        )
        val resp2 = client.post("/api/assignments") {
            contentType(ContentType.Application.Json)
            setBody(reqModel2)
        }
        val respModel2 = resp2.call.body<CreateAssignmentResponse>()
        val assignmentId = respModel2.assignmentId

        val resp3 = client.get("/api/assignments")
        val respModel3 = resp3.call.body<ListAssignmentsResponse>()
        assertEquals(
            listOf(AssignmentResponse(sampleName, sampleOldTimestamp, assignmentId)),
            respModel3.assignments
        )

        val resp4 = client.get("/api/assignments/$assignmentId")
        val respModel4 = resp4.call.body<GetAssignmentDetailsResponse>()
        respModel4.let { (name, text, publication, deadline, id) ->
            assertEquals(sampleName, name)
            assertEquals(sampleDescription, text)
            assertEquals(sampleOldTimestamp, publication)
            assertEquals(sampleOldTimestamp, deadline)
            assertEquals(assignmentId, id)
        }
    }

    @Test
    fun testAssignmentsFiltered() = testApplication {
        val client = doSetup()

        val reqModel1 = CreateAssignmentRequest(
            sampleName,
            sampleDescription,
            sampleFutureTimestamp,
            sampleFutureTimestamp,
            sampleChecker
        )
        client.post("/api/assignments") {
            contentType(ContentType.Application.Json)
            setBody(reqModel1)
        }

        val resp2 = client.get("/api/assignments")
        assertEquals(emptyList(), resp2.call.body<ListAssignmentsResponse>().assignments)
    }

    @Test
    fun testAssignmentsSorted() = testApplication {
        val client = doSetup()
        val t1 = Timestamp.parse("2020-01-01T00:00:00.00Z")
        val t2 = Timestamp.parse("2030-01-01T00:00:00.00Z")
        fun createRequest(t: Timestamp) = CreateAssignmentRequest(
            sampleName,
            sampleDescription,
            sampleOldTimestamp,
            t,
            null
        )

        client.post("/api/assignments") {
            contentType(ContentType.Application.Json)
            setBody(createRequest(t2))
        }
        client.post("/api/assignments") {
            contentType(ContentType.Application.Json)
            setBody(createRequest(t1))
        }

        val deadlines = client
            .get("/api/assignments")
            .call.body<ListAssignmentsResponse>()
            .assignments
            .map { it.deadlineTimestamp }
        assertEquals(listOf(t1, t2), deadlines)
    }

    @Test
    fun testSubmissions() = testApplication {
        val client = doSetup()

        val resp1 = client.get("/api/submissions")
        val respModel1 = resp1.call.body<ListSubmissionsResponse>()
        assertEquals(emptyList(), respModel1.submissions)

        val reqModel2 =
            CreateAssignmentRequest(
                sampleName,
                sampleDescription,
                sampleFutureTimestamp,
                sampleFutureTimestamp,
                sampleChecker
            )
        val resp2 = client.post("/api/assignments") {
            contentType(ContentType.Application.Json)
            setBody(reqModel2)
        }
        val respModel2 = resp2.call.body<CreateAssignmentResponse>()

        val reqModel3 = SubmitSubmissionRequest(respModel2.assignmentId, "http://sample.link")
        val resp3 = client.post("/api/submissions") {
            contentType(ContentType.Application.Json)
            setBody(reqModel3)
        }
        val respModel3 = resp3.call.body<SubmitSubmissionResponse>()
        val submId = respModel3.submissionId

        val resp4 = client.get("/api/submissions")
        val respModel4 = resp4.call.body<ListSubmissionsResponse>()
        assertEquals(1, respModel4.submissions.size)
        val submResp4 = respModel4.submissions[0]
        assertEquals(sampleName, submResp4.assignmentName)
        assertEquals(null, submResp4.success)
        assertEquals(submId, submResp4.id)

        val resp5 = client.get("/api/submissions/$submId")
        val respModel5 = resp5.call.body<GetSubmissionDetailsResponse>()
        assertEquals(null, respModel5.checkResultResponse)
        val submResp5 = respModel5.submissionResponse
        assertEquals(submResp4, submResp5)
    }

    @Test
    fun testSubmissionsSorted() = testApplication {
        val client = doSetup()

        val assignmentId = client.post("/api/assignments") {
            contentType(ContentType.Application.Json)
            setBody(
                CreateAssignmentRequest(
                    sampleName,
                    sampleDescription,
                    sampleOldTimestamp,
                    sampleFutureTimestamp,
                    null
                )
            )
        }.body<CreateAssignmentResponse>().assignmentId

        suspend fun submitSubmission() = client.post("/api/submissions") {
            contentType(ContentType.Application.Json)
            setBody(SubmitSubmissionRequest(assignmentId, "http://sample.link"))
        }

        submitSubmission()
        delay(100)
        submitSubmission()

        val timestamps = client
            .get("/api/submissions")
            .call.body<ListSubmissionsResponse>()
            .submissions
            .map { it.timestamp }
        assertEquals(2, timestamps.size)
        val (t1, t2) = timestamps
        assertTrue { t1 < t2 }
    }

    @Test
    fun testNoSuchSubmission() = testApplication {
        val client = doSetup()

        assertEquals(HttpStatusCode.NotFound, client.get("/api/submissions/5").call.response.status)
    }

    @Test
    fun testSubmissionToNonexistentAssignment() = testApplication {
        val client = doSetup()

        assertEquals(
            HttpStatusCode.BadRequest,
            client.post("/api/submissions") {
                contentType(ContentType.Application.Json)
                setBody(SubmitSubmissionRequest(5, "hehe://haha.hoho"))
            }.status
        )
    }

}