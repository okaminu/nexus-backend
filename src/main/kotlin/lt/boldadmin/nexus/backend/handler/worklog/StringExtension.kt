package lt.boldadmin.nexus.backend.handler.worklog

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.toLocalDate(): LocalDate = LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
