package com.example.mssiaan.service

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import org.springframework.stereotype.Service
import java.io.*
import java.net.URL


@Service
class GoogleCalendarService {

    private val APPLICATION_NAME = "calendar"

    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()


    private val TOKENS_DIRECTORY_PATH = "tokens"

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private val SCOPES: MutableList<String?>? = listOf(CalendarScopes.CALENDAR_READONLY, CalendarScopes.CALENDAR_EVENTS
    , CalendarScopes.CALENDAR).toMutableList()


    private val CREDENTIALS_FILE_PATH = "credential.json"


    @Throws(IOException::class)
    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport, authCode: String): Credential {
        val input: InputStream = getCredentialFilePath().openStream()
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(input))

        val flow = GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .build()
        val tokenResponse = flow.newTokenRequest(authCode).setRedirectUri("http://localhost:4200").execute()
        return flow.createAndStoreCredential(tokenResponse, "user")
    }

    @Throws(IOException::class)
    fun getAuthorizationUrl(): String {
        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()

        val input: InputStream = getCredentialFilePath().openStream()
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(input))

        val flow = GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .build()

        return flow.newAuthorizationUrl().setRedirectUri("http://localhost:4200").build()
    }

    fun getCredentialFilePath(): URL {
        return this::class.java.classLoader.getResource(CREDENTIALS_FILE_PATH)
                ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
    }

    fun getService(authCode: String): Calendar {
        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
        return Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT,authCode))
                .setApplicationName(APPLICATION_NAME)
                .build()
    }
}