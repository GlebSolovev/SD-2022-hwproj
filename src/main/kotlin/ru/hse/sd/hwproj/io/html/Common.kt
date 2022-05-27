package ru.hse.sd.hwproj.io.html

import kotlinx.html.*
import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.utils.Timestamp
import ru.hse.sd.hwproj.utils.formatToString
import java.time.Duration

private const val bootstrapCssCdn = "https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css"

fun HTML.customHead(block: HEAD.() -> Unit) {
    head {
        title("HwProj")
        meta { charset = "utf-8" } // crucial for correct checker uploading
        meta {
            name = "viewport"
            content = "width=device-width, initial-scale=1"
        }
        link(
            href = bootstrapCssCdn,
            rel = "stylesheet",
        )

        block()
    }
}

fun DIV.submissionsTable(submissions: List<SubmissionResponse>) {
    table("table") {
        tr {
            th { +"Id" }
            th { +"Assignment name" }
            th { +"Success" }
            th { } // link
        }
        for ((success, assignmentName, id, _) in submissions) {
            val colorTag = when (success) {
                null -> ""
                true -> "table-success"
                false -> "table-danger"
            }
            tr(colorTag) {
                td { +"$id" }
                td { +assignmentName }
                td { +"${success ?: "unknown"}" }
                td { a(href = "/student/submissions/$id") { +"see details" } }
            }
        }
    }
}

fun DIV.submissionDetails(response: GetSubmissionDetailsResponse) {
    val submission = response.submissionResponse
    val checkResult = response.checkResultResponse

    dl("row") {
        dt { +"For assignment" }
        dd { +submission.assignmentName }

        dt { +"Solution link" }
        dd { response.submissionLink } // TODO: <a>

        dt { +"Checking status" }
        dd { if (checkResult != null) p { +"Checker output: ${checkResult.output}" } }
    }
}

fun DIV.assignmentsTable(assignments: List<AssignmentResponse>, isStudent: Boolean) {
    table("table") {
        tr {
            th { +"Assignment name" }
            th { +"Deadline" }
            th { } // link to details
        }
        for ((name, deadline, id) in assignments) {
            val delta: Duration = Duration.between(Timestamp.now(), deadline)
            val colorTag: String = when {
                delta.isNegative -> "table-secondary"
                delta.toHours() < 24 -> "table-warning"
                else -> ""
            }
            tr(colorTag) {
                td { +name }
                td { +deadline.formatToString() }
                td {
                    a(href = "/${if (isStudent) "student" else "teacher"}/assignments/$id") {
                        +"See details${if (isStudent) " / submit" else ""}"
                    }
                }
            }
        }
    }
}

fun DIV.assignmentDetails(details: GetAssignmentDetailsResponse) {
    dl("row") {
        dt { +"Name" }
        dd { +details.name }

        dt { +"Deadline" }
        dd { +details.deadlineTimestamp.formatToString() }

        dt { +"Published" }
        dd { +details.publicationTimestamp.formatToString() }

        dt { +"Task text" }
        dd { +details.taskText }
    }
}

fun HTML.makeWelcomePage() {
    customHead { }
    body {
        padded {
            h1 { +"Welcome to HwProj!" }
            div { a(href = "/student") { +"Continue as student" } }
            div { a(href = "/teacher") { +"Continue as teacher" } }
        }
    }
}

inline fun BODY.padded(crossinline block: DIV.() -> Unit) {
    containerFluid("p-5") { block() }
}


@HtmlTagMarker
inline fun FlowContent.containerFluid(classes: String = "", crossinline block: DIV.() -> Unit = {}): Unit =
    DIV(attributesMapOf("class", "container-fluid $classes"), consumer).visit(block)
