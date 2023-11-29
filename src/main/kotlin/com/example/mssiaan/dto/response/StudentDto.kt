package com.example.mssiaan.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class StudentDto(
        var userId: Int,
        var nombre: String,
        var apellidoPaterno: String,
        var apellidoMaterno: String
){
    constructor(): this(0,"","","")
}
