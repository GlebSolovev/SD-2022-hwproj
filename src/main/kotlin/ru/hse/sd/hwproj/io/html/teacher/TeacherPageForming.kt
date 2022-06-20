package ru.hse.sd.hwproj.io.html.teacher

import kotlinx.html.*
import ru.hse.sd.hwproj.io.html.*
import ru.hse.sd.hwproj.models.*

/**
 * Generates a teacher HTML navigation bar.
 */
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

/**
 * Self-explanatory.
 */
fun HTML.makeTeacherAssignmentsPage(response: ListAssignmentsResponse) {
    val assignments = response.assignments
    customHead { }
    body {
        teacherNavbar()
        padded {
            h1 { +"Assignments" }
            div("p-3") { a(href = "/teacher/assignments/new") { +"Create new assignment" } }
            div {
                assignmentsTable(assignments, false)
            }
        }
    }
}

/**
 * Generates a teacher HTML page for creating a new assignment.
 */
fun HTML.makeTeacherNewAssignmentPage() {
    customHead { }
    body {
        teacherNavbar()
        padded {
            h1 { +"Creating new assignment" }
            div("d-flex justify-content-start") {
                form(
                    action = "/teacher/assignments/new",
                    method = FormMethod.post,
                    encType = FormEncType.multipartFormData,
                ) {
                    p {
                        label { +"Assignment name:" }
                        textInput(name = "name", classes = "form-control") { required = true }
                    }
                    p {
                        label { +"Task text:" }
                        textInput(name = "taskText", classes = "form-control") { required = true }
                    }
                    p {
                        label { +"Publication time:" }
                        dateTimeLocalInput(name = "publicationTime", classes = "form-control") { required = true }
                    }
                    p {
                        label { +"Deadline:" }
                        dateTimeLocalInput(name = "deadlineTime", classes = "form-control") { required = true }
                    }
                    p {
                        label { +"Checker program:" }
                        fileInput(name = "checker", classes = "form-control")
                    }
                    p {
                        submitInput(classes = "form-control btn btn-primary") { value = "Create new assignment" }
                    }
                }
            }
        }
    }
}

/**
 * Generates a teacher HTML page that is shown when a new assignment is created.
 */
fun HTML.makeTeacherCreatedAssignmentPage(response: CreateAssignmentResponse) {
    customHead { }
    body {
        teacherNavbar()
        padded {
            h1 { +"Creating new assignment" }
            div("my-2") { +"Successfully created new assignment with id #${response.assignmentId}" }
            div { a(href = "/teacher/assignments") { +"Back to assignments list" } }
        }
    }
}

/**
 * Self-explanatory.
 */
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

/**
 * Self-explanatory.
 */
fun HTML.makeTeacherSubmissionListPage(response: ListSubmissionsResponse) {
    val submissions = response.submissions
    customHead { }
    body {
        teacherNavbar()
        padded {
            h1 { +"Submissions list" }
            div {
                submissionsTable(submissions, false)
            }
        }
    }
}

/**
 * Self-explanatory.
 */
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
