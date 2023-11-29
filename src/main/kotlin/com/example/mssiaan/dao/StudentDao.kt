package com.example.mssiaan.dao

import com.example.mssiaan.dto.response.StudentDto
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Component

@Mapper
@Component
interface StudentDao {

    /*userid, nombre, apellido paterno, apellido materno*/

    @Select("SELECT u.user_id, per.nombre, per.apellido_paterno, per.apellido_materno " +
            "FROM persona per " +
            "INNER JOIN usuario u ON per.id_persona = u.persona_id " +
            "INNER JOIN estudiante est ON u.user_id = est.user_id " )
    fun getStudents(): List<StudentDto>

}