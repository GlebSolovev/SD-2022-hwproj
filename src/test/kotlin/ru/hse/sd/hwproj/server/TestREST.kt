@file:UseSerializers(TimestampSerializer::class)

package ru.hse.sd.hwproj.server

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*

import kotlin.io.path.createTempDirectory
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

import ru.hse.sd.hwproj.interactor.EntityGateway
import ru.hse.sd.hwproj.interactor.Interactor
import ru.hse.sd.hwproj.models.AssignmentResponse
import ru.hse.sd.hwproj.models.CreateAssignmentRequest
import ru.hse.sd.hwproj.models.CreateAssignmentResponse
import ru.hse.sd.hwproj.models.ListAssignmentsResponse
import ru.hse.sd.hwproj.storage.sqlite.SQLiteStorage
import ru.hse.sd.hwproj.utils.CheckerProgram
import ru.hse.sd.hwproj.utils.Timestamp
import ru.hse.sd.hwproj.utils.TimestampSerializer

import kotlinx.serialization.UseSerializers

class TestREST {

    private val tempDir = createTempDirectory("hwproj-rest-test")
    private lateinit var storage: SQLiteStorage

    private val sampleName = "Sample Name"
    private val sampleDescription = "sample description"
    private val sampleTimestamp = Timestamp.parse("2022-05-23T16:58:30.00Z")

    private val sampleChecker = CheckerProgram(byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7))

    @BeforeTest
    fun generateNewSQLiteStorage() {
        storage = SQLiteStorage(kotlin.io.path.createTempFile(tempDir).toAbsolutePath().toString())
    }

    private fun ApplicationTestBuilder.doSetup(): HttpClient {
        application {
            createRESTModule(Interactor(EntityGateway(storage)))
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

        val reqModel2 =
            CreateAssignmentRequest(sampleName, sampleDescription, sampleTimestamp, sampleTimestamp, sampleChecker)
        val resp2 = client.post("/api/assignments") {
            contentType(ContentType.Application.Json)
            setBody(reqModel2)
        }
        val respModel2 = resp2.call.body<CreateAssignmentResponse>()

        val resp3 = client.get("/api/assignments")
        val respModel3 = resp3.call.body<ListAssignmentsResponse>()
        assertEquals(
            listOf(AssignmentResponse(sampleName, sampleTimestamp, respModel2.assignmentId)),
            respModel3.assignments
        )
    }

}