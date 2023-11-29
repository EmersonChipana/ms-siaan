package com.example.mssiaan.controller

import com.example.mssiaan.dto.response.ResponseDto
import com.example.mssiaan.dto.response.StudentDto
import com.example.mssiaan.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/student")
class StudentController @Autowired constructor(
        private val studentService: StudentService
) {


    @GetMapping("/all")
    fun getAllStudents(): ResponseEntity<ResponseDto<List<StudentDto>>>{
        val students = studentService.getAllStudents()
        return ResponseEntity.ok(
                ResponseDto<List<StudentDto>>(
                        data = students,
                        success = true,
                        message = "Estudiantes obtenidos",
                        error = ""
                )
        )
    }
}