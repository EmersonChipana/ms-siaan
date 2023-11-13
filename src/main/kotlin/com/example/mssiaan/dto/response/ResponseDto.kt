package com.example.mssiaan.dto.response

data class ResponseDto<T>(
        val message: String,
        val data: T,
        val success: Boolean,
        val error: String
)
