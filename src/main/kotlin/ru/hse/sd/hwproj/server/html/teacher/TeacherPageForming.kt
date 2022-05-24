package ru.hse.sd.hwproj.server.html.teacher

import kotlinx.html.*
import ru.hse.sd.hwproj.models.CreateAssignmentResponse
import ru.hse.sd.hwproj.models.ListAssignmentsResponse
import ru.hse.sd.hwproj.server.html.assignmentsTable

fun HTML.makeTeacherAssignmentsPage(response: ListAssignmentsResponse) {
    val assignments = response.assignments
    head {
        title("HwProj")
    }
    body {
        h1 { +"Assignments" }
        div { a(href = "/teacher/assignments/new") { +"Create new assignment" } }
        div { assignmentsTable(assignments) }
    }
}

fun HTML.makeTeacherNewAssignmentPage() {
    head {
        title("HwProj")
    }
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
    head {
        title("HwProj")
    }
    body {
        h1 { +"Creating new assignment" }
        div { +"Successfully created new assignment with id #${response.assignmentId}" }
        div { a(href = "/teacher/assignments") { +"Back to assignments list" } }
    }
}
