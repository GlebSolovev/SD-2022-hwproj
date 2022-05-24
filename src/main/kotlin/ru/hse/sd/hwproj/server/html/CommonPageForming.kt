package ru.hse.sd.hwproj.server.html

import kotlinx.html.*
import ru.hse.sd.hwproj.models.AssignmentResponse
import ru.hse.sd.hwproj.models.GetSubmissionDetailsResponse
import ru.hse.sd.hwproj.models.ListSubmissionsResponse
import ru.hse.sd.hwproj.models.SubmissionResponse
import ru.hse.sd.hwproj.utils.formatToString

fun DIV.assignmentsTable(assignments: List<AssignmentResponse>) {
    table {
        tr {
            th { +"Assignment name" }
            th { +"Deadline" }
        }
        for ((name, deadline, _) in assignments) {
            tr {
                td { +name }
                td { +deadline.formatToString() }
            }
        }
    }
}

fun DIV.submissionsTable(submissions: List<SubmissionResponse>) {
    table {
        tr {
            th { +"Id" }
            th { +"Assignment name" }
            th { +"Success" }
            th { } // link
        }
        for ((success, assignmentName, id, _) in submissions) {
            tr {
                td { +"$id" }
                td { +assignmentName }
                td { +"${success ?: "unknown"}" }
                td { a(href = "/student/submissions/$id") { +"see details" } }
            }
        }
    }
}

fun HTML.makeListSubmissionsPage(response: ListSubmissionsResponse) {
    val submissions = response.submissions
    head {
        title("HwProj")
    }
    body {
        h1 { +"Submissions list" }
        div {
            submissionsTable(submissions)
        }
    }
}

fun HTML.makeSubmissionDetailsPage(response: GetSubmissionDetailsResponse) {
    val submission = response.submissionResponse
    val checkResult = response.checkResultResponse

    head {
        title("HwProj")
    }
    body {
        h1 { +"Details for submission #${submission.id}" }
        div {
            p { +"For assignment: ${submission.assignmentName}" }
            p { +"Success: ${checkResult?.success ?: "unknown"}" }
            if (checkResult != null) p { +"Checker output: ${checkResult.output}" }
        }
    }
}
