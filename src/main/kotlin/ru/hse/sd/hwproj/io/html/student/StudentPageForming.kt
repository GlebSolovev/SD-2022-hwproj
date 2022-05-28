package ru.hse.sd.hwproj.io.html.student

import kotlinx.html.*
import ru.hse.sd.hwproj.io.html.*
import ru.hse.sd.hwproj.models.*

fun BODY.studentNavbar() {
    nav(classes = "navbar navbar-expand-lg navbar-dark bg-info") {
        containerFluid {
            span("navbar-brand m-3 h1") { +"Student mode" }
            div("navbar-nav") {
                a(href = "/student/assignments", classes = "nav-item m-3") { +"Assignments" }
                a(href = "/student/submissions", classes = "nav-item m-3") { +"Submissions" }
            }
        }
    }
}

fun HTML.makeStudentAssignmentsPage(response: ListAssignmentsResponse) {
    val assignments = response.assignments
    customHead { }
    body {
        studentNavbar()
        padded {
            h1 {
                +"Assignments list"
            }
            div {
                assignmentsTable(assignments, true)
            }
        }
    }
}

fun HTML.makeStudentAssignmentDetailsPage(response: GetAssignmentDetailsResponse) {
    customHead { }
    body {
        studentNavbar()
        padded {
            div("container m-0 p-0") {
                div("row m-0 p-0") {
                    div("col-9 m-0 p-0") {
                        h1 {
                            +"Details for assignment #${response.id}"
                        }
                        div {
                            assignmentDetails(response)
                        }
                    }
                    div("col-3") {
                        h2 { +"Create new submission:" }
                        form(
                            action = "/student/submissions",
                            method = FormMethod.post,
                            encType = FormEncType.applicationXWwwFormUrlEncoded,
                        ) {
                            p("my-4") {
                                label { +"Link to solution:" }
                                textInput(name = "link", classes = "form-control") { required = true }
                            }
                            hiddenInput(name = "assignmentId") { value = "${response.id}" }
                            submitInput(classes = "form-control btn btn-primary") { value = "Submit" }
                        }
                    }
                }
            }
        }
    }
}

fun HTML.makeStudentSubmittedSubmissionPage(response: SubmitSubmissionResponse) {
    customHead { }
    body {
        studentNavbar()
        padded {
            h1 {
                +"Created new submission"
            }
            div("my-2") {
                +"Successfully submitted new submission with id #${response.submissionId}"
            }
            div {
                a(href = "/student/submissions") { +"Go to submissions list" }
            }
        }
    }
}

fun HTML.makeStudentSubmissionListPage(response: ListSubmissionsResponse) {
    val submissions = response.submissions
    customHead { }
    body {
        studentNavbar()
        padded {
            h1 { +"Submissions list" }
            div {
                submissionsTable(submissions, true)
            }
        }
    }
}

fun HTML.makeStudentSubmissionDetailsPage(response: GetSubmissionDetailsResponse) {
    customHead { }
    body {
        studentNavbar()

        padded {
            h1 { +"Details for submission #${response.submissionResponse.id}" }
            div {
                submissionDetails(response)
            }
        }
    }
}
