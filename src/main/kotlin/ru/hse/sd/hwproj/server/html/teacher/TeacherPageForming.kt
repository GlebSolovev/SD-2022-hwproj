package ru.hse.sd.hwproj.server.html.teacher

import kotlinx.html.*
import ru.hse.sd.hwproj.models.CreateAssignmentResponse
import ru.hse.sd.hwproj.models.GetAssignmentDetailsResponse
import ru.hse.sd.hwproj.models.ListAssignmentsResponse
import ru.hse.sd.hwproj.server.html.assignmentDetails
import ru.hse.sd.hwproj.server.html.customHead
import ru.hse.sd.hwproj.utils.formatToString

fun HTML.makeTeacherAssignmentsPage(response: ListAssignmentsResponse) {
    val assignments = response.assignments
    customHead {  }
    body {
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

fun HTML.makeTeacherNewAssignmentPage() {
    customHead {  }
    body {
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

fun HTML.makeTeacherCreatedAssignmentPage(response: CreateAssignmentResponse) {
    customHead {  }
    body {
        h1 { +"Creating new assignment" }
        div { +"Successfully created new assignment with id #${response.assignmentId}" }
        div { a(href = "/teacher/assignments") { +"Back to assignments list" } }
    }
}

fun HTML.makeTeacherAssignmentDetailsPage(response: GetAssignmentDetailsResponse) {
    customHead {  }
    body {
        h1 {
            +"Details for assignment #${response.id}"
        }
        div {
            assignmentDetails(response)
        }
    }
}

fun HTML.makeTeacherLandingPage() {
    customHead {  }
    body {
        h1 {
            +"Teacher mode"
        }
        div { a(href = "/teacher/assignments") { +"See assignments list" } }
        div { a(href = "/teacher/submissions") { +"See submissions list" } }
    }
}
