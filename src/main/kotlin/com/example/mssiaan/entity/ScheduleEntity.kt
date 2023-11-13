package com.example.mssiaan.entity

import java.sql.Timestamp

data class ScheduleEntity(
    var userId: Int,
    var numParalelo: Int,
    var docenteNombre: String,
    var nombreMateria: String,
    var siglaMateria: String,
    var aulaDiaHorarioId: Int,
    var horaInicio: String,
    var horaFin: String,
    var bloque: String,
    var numeroAula: String,
    var dia: String
)
