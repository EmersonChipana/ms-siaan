package com.example.mssiaan.dto.response

data class ScheduleDto(
    var day: String,
    var subjects: List<SubjectDto>
)

data class SubjectDto(
    var name: String,
    var code: String,
    var teacher: String,
    var start: String,
    var end: String,
    var classroom: String,
)
