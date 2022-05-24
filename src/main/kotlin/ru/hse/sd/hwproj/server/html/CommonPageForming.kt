package ru.hse.sd.hwproj.server.html

import kotlinx.html.*
import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.utils.formatToString

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
            p {
                // TODO: <a>
                +"Link to solution: ${response.submissionLink}"
            }
            p { +"Success: ${checkResult?.success ?: "unknown"}" }
            if (checkResult != null) p { +"Checker output: ${checkResult.output}" }
        }
    }
}

fun DIV.assignmentDetails(details: GetAssignmentDetailsResponse) {
    p { +"Name: ${details.name}" }
    p { +"Deadline: ${details.deadlineTimestamp.formatToString()}" }
    p { +"Published: ${details.publicationTimestamp.formatToString()}" }
    p {
        label { +"Task:" }
        hardTextArea {
            disabled = true
            +details.taskText
        }
    }
}