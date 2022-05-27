package ru.hse.sd.hwproj.io.html

import kotlinx.html.*
import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.utils.formatToString

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

fun DIV.submissionDetails(response: GetSubmissionDetailsResponse) {
    val submission = response.submissionResponse
    val checkResult = response.checkResultResponse

    p { +"For assignment: ${submission.assignmentName}" }
    p {
        // TODO: <a>
        +"Link to solution: ${response.submissionLink}"
    }
    p { +"Success: ${checkResult?.success ?: "unknown"}" }
    if (checkResult != null) p { +"Checker output: ${checkResult.output}" }
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

fun HTML.makeWelcomePage() {
    customHead { }
    body {
        h1 { +"Welcome to HwProj!" }
        div { a(href = "/student") { +"Continue as student" } }
        div { a(href = "/teacher") { +"Continue as teacher" } }
    }
}

@HtmlTagMarker
inline fun FlowContent.containerFluid(classes: String = "", crossinline block: DIV.() -> Unit = {}): Unit =
    DIV(attributesMapOf("class", "container-fluid $classes"), consumer).visit(block)
