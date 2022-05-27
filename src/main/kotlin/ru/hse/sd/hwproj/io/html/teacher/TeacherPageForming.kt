package ru.hse.sd.hwproj.io.html.teacher

import kotlinx.html.*
import ru.hse.sd.hwproj.io.html.*
import ru.hse.sd.hwproj.models.*
import ru.hse.sd.hwproj.utils.formatToString

fun BODY.teacherNavbar() {
    nav(classes = "navbar navbar-expand-lg navbar-light bg-light") {
        containerFluid {
            span("navbar-brand mx-3 h1") { +"Teacher mode" }
            div("navbar-nav") {
                a(href = "/teacher/assignments", classes = "nav-item m-3") { +"Assignments" }
                a(href = "/teacher/submissions", classes = "nav-item m-3") { +"Submissions" }
            }
        }
    }
}

fun HTML.makeTeacherAssignmentsPage(response: ListAssignmentsResponse) {
    val assignments = response.assignments
    customHead { }
    body {
        teacherNavbar()
        padded {
            h1 { +"Assignments" }
            div { a(href = "/teacher/assignments/new") { +"Create new assignment" } }
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
                            td { a(href = "/teacher/assignments/$id") { +"See details" } }
                        }
                    }
                }
            }
        }
    }
}

fun HTML.makeTeacherNewAssignmentPage() {
    customHead { }
    body {
        teacherNavbar()
        padded {
            h1 { +"Creating new assignment" }
            div {
                form(
                    action = "/teacher/assignments/new",
                    method = FormMethod.post,
                    encType = FormEncType.applicationXWwwFormUrlEncoded,
                ) {
                    p {
                        label { +"Assignment name:" }
                        textInput(name = "name") { required = true }
                    }
                    p {
                        label { +"Task text:" }
                        textInput(name = "taskText") { required = true }
                    }
                    p {
                        label { +"Publication time:" }
                        dateTimeLocalInput(name = "publicationTime") { required = true }
                    }
                    p {
                        label { +"Deadline:" }
                        dateTimeLocalInput(name = "deadlineTime") { required = true }
                    }
                    p {
                        label { +"Checker program:" }
                        fileInput(name = "checker")
                    }
                    p {
                        submitInput { value = "Create new assignment" }
                    }
                }
            }
        }
    }
}

fun HTML.makeTeacherCreatedAssignmentPage(response: CreateAssignmentResponse) {
    customHead { }
    body {
        teacherNavbar()
        padded {
            h1 { +"Creating new assignment" }
            div { +"Successfully created new assignment with id #${response.assignmentId}" }
            div { a(href = "/teacher/assignments") { +"Back to assignments list" } }
        }
    }
}

fun HTML.makeTeacherAssignmentDetailsPage(response: GetAssignmentDetailsResponse) {
    customHead { }
    body {
        teacherNavbar()
        padded {
            h1 {
                +"Details for assignment #${response.id}"
            }
            div {
                assignmentDetails(response)
            }
        }
    }
}

fun HTML.makeTeacherSubmissionListPage(response: ListSubmissionsResponse) {
    val submissions = response.submissions
    customHead { }
    body {
        teacherNavbar()
        padded {
            h1 { +"Submissions list" }
            div {
                submissionsTable(submissions)
            }
        }
    }
}

fun HTML.makeTeacherSubmissionDetailsPage(response: GetSubmissionDetailsResponse) {
    customHead { }
    body {
        teacherNavbar()
        padded {
            h1 { +"Details for submission #${response.submissionResponse.id}" }
            div {
                submissionDetails(response)
            }
        }
    }
}
