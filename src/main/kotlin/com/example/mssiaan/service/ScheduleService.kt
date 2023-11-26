package com.example.mssiaan.service

import com.example.mssiaan.dao.ScheduleDao
import com.example.mssiaan.dto.response.ScheduleDto
import com.example.mssiaan.dto.response.SubjectDto
import com.example.mssiaan.entity.ScheduleEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScheduleService @Autowired constructor(
    private val scheduleDao: ScheduleDao
){

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getSchedule(userId: Int): List<ScheduleEntity>{
        try{
            logger.info("Obteniendo horarios del usuario: $userId")
            return scheduleDao.getSchedule(userId)
        } catch (e: Exception){
            logger.error("Error al obtener los horarios del usuario: $userId")
            throw Exception("Error al obtener los horarios del usuario: $userId")
        }
    }

    fun getScheduleForCalendar(userId: Int): List<ScheduleDto>{
        val schedule = getSchedule(userId)
        var scheduleForCalendar = mutableListOf<ScheduleDto>()
        schedule.forEach { it ->
            val subjectDto = SubjectDto(
                    name = it.nombreMateria,
                    code = it.siglaMateria,
                    teacher = it.docenteNombre,
                    start = it.horaInicio,
                    end = it.horaFin,
                    classroom = it.numeroAula
            )
            val scheduleDto = ScheduleDto(
                    day = it.dia,
                    subjects = listOf(subjectDto)
            )
            var scheduleDtoFound = scheduleForCalendar.find { sch -> scheduleDto.day == sch.day }
            if (scheduleDtoFound != null){
                if(!scheduleDtoFound.subjects.contains(subjectDto)){
                    scheduleDtoFound.subjects = scheduleDtoFound.subjects + subjectDto
                } else {
                    logger.info("Subject already exists")
                }
            } else {
                scheduleForCalendar.add(scheduleDto)
            }
        }
        return scheduleForCalendar
    }
}