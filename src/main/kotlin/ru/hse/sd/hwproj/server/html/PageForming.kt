package ru.hse.sd.hwproj.server.html

import kotlinx.html.*
import ru.hse.sd.hwproj.models.ListAssignmentsResponse
import ru.hse.sd.hwproj.utils.formatToString

fun HTML.formListAssignmentsPage(response: ListAssignmentsResponse) {
    val assignments = response.assignments
    head {
        title("HwProj")
    }
    body {
        h1 {
            +"Assignments list"
        }
        div {
            table {
                for((name, deadline, _) in assignments) {
                    tr {
                        td { +name }
                        td { +deadline.formatToString() }
                    }
                }
            }
        }
    }
}
