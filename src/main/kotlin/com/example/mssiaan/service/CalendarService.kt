package com.example.mssiaan.service

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventAttendee
import com.google.api.services.calendar.model.EventDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class CalendarService @Autowired constructor(
    private val googleCalendarService: GoogleCalendarService
){

    fun createCalendar(email: String, authCode: String){
        val service = googleCalendarService.getService(authCode)
        val event = Event()

        event.setSummary("Pruebas 2")
        event.setLocation("Universidad Catolica Boliviana San Pablo")

        val attendees = ArrayList<EventAttendee>()
        attendees.add(EventAttendee().setEmail(email))

        event.setAttendees(attendees)

        val start = DateTime.parseRfc3339("2023-11-14T10:00:00.000-07:00")
        val end = DateTime.parseRfc3339("2023-11-14T10:25:00.000-07:00")
        event.setStart(EventDateTime().setDateTime(start).setTimeZone("America/Los_Angeles"))
        event.setEnd(EventDateTime().setDateTime(end).setTimeZone("America/Los_Angeles"))
        event.setRecurrence(mutableListOf("RRULE:FREQ=WEEKLY;UNTIL=20231201T170000Z"))

        val recurringEvent: Event = service.events().insert("primary", event).execute()
    }

}