package ru.hse.sd.hwproj.server.html.student

import kotlinx.html.*
import ru.hse.sd.hwproj.models.ListAssignmentsResponse
import ru.hse.sd.hwproj.server.html.assignmentsTable

fun HTML.makeStudentAssignmentsPage(response: ListAssignmentsResponse) {
    val assignments = response.assignments
    head {
        title("HwProj")
    }
    body {
        h1 {
            +"Assignments list"
        }
        div { assignmentsTable(assignments) }
    }
}


