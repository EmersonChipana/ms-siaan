package com.example.mssiaan.service

import com.example.mssiaan.dao.StudentDao
import com.example.mssiaan.dto.response.StudentDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StudentService @Autowired constructor(
        private val studentDao: StudentDao
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getAllStudents(): List<StudentDto>{
        try {
            logger.info("Obteniendo todos los estudiantes")
            return studentDao.getStudents()
        } catch (e: Exception) {
            logger.error("Error al obtener todos los estudiantes "+ e.message)
            throw Exception("Error al obtener todos los estudiantes " + e.message)
        }
    }
}