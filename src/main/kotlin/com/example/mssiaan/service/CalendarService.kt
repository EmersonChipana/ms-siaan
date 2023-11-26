package com.example.mssiaan.service

import com.example.mssiaan.dto.response.SubjectDto
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.*
import com.google.api.services.calendar.model.Calendar
import com.google.api.services.calendar.model.Event.Reminders
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import java.util.*
import com.google.api.services.calendar.Calendar as GoogleCalendar


@Service
class CalendarService @Autowired constructor(
    private val googleCalendarService: GoogleCalendarService,
    private val scheduleService: ScheduleService
){

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun createCalendar(service: GoogleCalendar): String{
        val calendar = Calendar()

        calendar.setSummary("UCB ${getStringSemester()}")
        calendar.setTimeZone("America/La_Paz")
        var createdCalendar: Calendar
        try{
            createdCalendar = service.calendars().insert(calendar).execute()
        } catch (e: Exception){
            logger.error("Error al crear el calendario: ${e.message}")
            throw Exception("Error al crear el calendario")
        }
        logger.info("Created calendar id: ${createdCalendar.id}")
        return createdCalendar.id
    }

    fun createListEvents(authCode: String, userId: Int, email: Boolean,emailHours: Int, pop: Boolean, popMin: Int){
        val service = googleCalendarService.getService(authCode)
        val calendarId = createCalendar(service)
        val schedule = scheduleService.getScheduleForCalendar(userId)
        schedule.forEach { sch ->
            sch.subjects.forEach{
                createEvent(it, sch.day, email,emailHours, pop,popMin, service, calendarId)
            }
        }
    }

    fun getStart(day: String): String{
        val now = LocalDateTime.now()
        val year = now.year
        val firstSemester = LocalDate.of(year,1,1).with(TemporalAdjusters.firstInMonth(getDay(day)))
        val secondSemester = LocalDate.of(year,7,1).with(TemporalAdjusters.firstInMonth(getDay(day)))

        val month = now.monthValue
        return if (month <= 6) "${year}-02-0${firstSemester.dayOfMonth}T" else "${year}-08-0${secondSemester.dayOfMonth}T"
    }

    fun getDay(dat: String): DayOfWeek{
        return when(dat){
            "Lunes" -> DayOfWeek.MONDAY
            "Martes" -> DayOfWeek.TUESDAY
            "Miercoles" -> DayOfWeek.WEDNESDAY
            "Jueves" -> DayOfWeek.THURSDAY
            "Viernes" -> DayOfWeek.FRIDAY
            "Sabado" -> DayOfWeek.SATURDAY
            else -> DayOfWeek.MONDAY
        }
    }

    fun getUntil(): String {
        val date = LocalDateTime.now()
        val year = date.year
        val month = date.monthValue
        return if (month <= 6) "${year}0631T235900Z" else "${year}1220T235900Z"
    }

    fun createEvent(it: SubjectDto, sch: String, email: Boolean,emailHours: Int, pop: Boolean,popMin: Int, service: GoogleCalendar, calendarId: String){
        val event = Event()
        event.setSummary(it.code)
        event.setDescription(it.name + " - " + it.teacher)
        event.setLocation("Universidad Católica Boliviana San Pablo - "+it.classroom)
        val start = DateTime.parseRfc3339("${getStart(sch)}${it.start}.000-04:00")
        val end = DateTime.parseRfc3339("${getStart(sch)}${it.end}.000-04:00")
        val reminderOverrides = ArrayList<EventReminder>()
        if(email) reminderOverrides.add(EventReminder().setMethod("email").setMinutes(emailHours* 60))
        if(pop) reminderOverrides.add(EventReminder().setMethod("popup").setMinutes(popMin))
        val reminders: Reminders = Reminders()
            .setUseDefault(false)
            .setOverrides(reminderOverrides.toList())
        event.setReminders(reminders)
        event.setStart(EventDateTime().setDateTime(start).setTimeZone("America/La_Paz"))
        event.setEnd(EventDateTime().setDateTime(end).setTimeZone("America/La_Paz"))
        event.setRecurrence(mutableListOf("RRULE:FREQ=WEEKLY;UNTIL=${getUntil()}"))
        try{
            logger.info("Creando el evento: ${it.name}")
            service.events().insert(calendarId, event).execute()
        } catch (e: Exception){
            logger.error("Error al crear el evento: ${e.message}")
            throw Exception("Error al crear el evento")
        }
    }

    fun getStringSemester(): String{
        val date = LocalDateTime.now()
        val year = date.year
        val month = date.monthValue
        val semester = if(month <= 6) "I" else "II"
        return "$year - $semester"
    }

}