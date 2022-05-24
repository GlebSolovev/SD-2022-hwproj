package ru.hse.sd.hwproj.server.html.student

import kotlinx.html.*
import ru.hse.sd.hwproj.models.GetAssignmentDetailsResponse
import ru.hse.sd.hwproj.models.ListAssignmentsResponse
import ru.hse.sd.hwproj.models.SubmitSubmissionResponse
import ru.hse.sd.hwproj.server.html.assignmentDetails
import ru.hse.sd.hwproj.server.html.customHead
import ru.hse.sd.hwproj.utils.formatToString

fun HTML.makeStudentAssignmentsPage(response: ListAssignmentsResponse) {
    val assignments = response.assignments
    customHead {  }
    body {
        h1 {
            +"Assignments list"
        }
        div {
            table {
                tr {
                    th { +"Assignment name" }
                    th { +"Deadline" }
                    th { } // link to details
                }
                for ((name, deadline, id) in assignments) {
                    tr {
                        td { +name }
                        td { +deadline.formatToString() }
                        td { a(href = "/student/assignments/$id") { +"See details / submit" } }
                    }
                }
            }
        }
    }
}

fun HTML.makeStudentAssignmentDetailsPage(response: GetAssignmentDetailsResponse) {
    customHead {  }
    body {
        h1 {
            +"Details for assignment #${response.id}"
        }
        div {
            assignmentDetails(response)
        }
        div {
            h2 { +"Create new submission:" }
            form(
                action = "/student/submissions",
                method = FormMethod.post,
                encType = FormEncType.applicationXWwwFormUrlEncoded,
            ) {
                p {
                    label { +"Link to solution:" }
                    textInput(name = "link")
                }
                hiddenInput(name = "assignmentId") { value = "${response.id}" }
                submitInput { value = "Submit" }
            }
        }
    }
}

fun HTML.makeStudentSubmittedSubmissionPage(response: SubmitSubmissionResponse) {
    customHead {  }
    body {
        h1 {
            +"Created new submission"
        }
        div {
            +"Successfully submitted new submission with id #${response.submissionId}"
        }
        div {
            a(href = "/student/submissions") { +"Go to submissions list" }
        }
    }
}

fun HTML.makeStudentLandingPage() {
    customHead {  }
    body {
        h1 {
            +"Student mode"
        }
        div { a(href = "/student/assignments") { +"See assignments list" } }
        div { a(href = "/student/submissions") { +"See submissions list" } }
    }
}
