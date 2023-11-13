package com.example.mssiaan.controller

import com.example.mssiaan.dto.response.ResponseDto
import com.example.mssiaan.service.CalendarService
import com.example.mssiaan.service.GoogleCalendarService
import com.example.mssiaan.service.ScheduleService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/schedule")
class ScheduleController @Autowired constructor(
    private val scheduleService: ScheduleService,
        private val calendarService: CalendarService,
        private val googleCalendarService: GoogleCalendarService
){

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{userId}")
    fun getSchedule(@PathVariable userId:Int): ResponseEntity<List<*>>{
        val schedule = scheduleService.getScheduleForCalendar(userId)
        return ResponseEntity.ok(schedule)
    }

    @GetMapping("/create/{userId}")
    fun sendScheduloToCalendar(
            @PathVariable userId:Int,
            @RequestParam authCode: String
    ): ResponseEntity<ResponseDto<String>>{
        val schedule = calendarService.createCalendar("emersonchipana12345@gmail.com", authCode)

        return ResponseEntity.ok(
                ResponseDto<String>(
                        data = "Evento Creado correctamente",
                        success = true,
                        message = "Horario creado",
                        error = ""
                )
        )
    }

    @GetMapping("/google/auth")
    fun getUrlAuth(): ResponseEntity<ResponseDto<String>>{
        val url = googleCalendarService.getAuthorizationUrl()
        return ResponseEntity.ok(
                ResponseDto<String>(
                        data = url,
                        success = true,
                        message = "Url de autorizacion",
                        error = ""
                )
        )
    }

}