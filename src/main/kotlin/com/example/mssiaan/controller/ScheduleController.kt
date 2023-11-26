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
            @RequestParam authCode: String,
            @RequestParam(required = false) email: Boolean = false,
            @RequestParam(required = false) emailHours: Int = 24,
            @RequestParam(required = false) pop: Boolean = false,
            @RequestParam(required = false) popMin: Int = 10
    ): ResponseEntity<ResponseDto<String>>{
        val schedule = calendarService.createListEvents(authCode, userId, email,emailHours, pop, popMin)

        return ResponseEntity.ok(
                ResponseDto<String>(
                        data = "Calendario creado con exito",
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