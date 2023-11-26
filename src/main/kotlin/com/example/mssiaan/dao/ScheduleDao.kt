package com.example.mssiaan.dao

import com.example.mssiaan.entity.ScheduleEntity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component


@Mapper
@Component
interface ScheduleDao {

    @Select("SELECT us.user_id, par.num_paralelo, " +
            "person.nombre as docente_nombre, mat.nombre_materia, mat.sigla_materia, " +
            "adh.aula_dia_horario_id ,hor.hora_inicio, hor.hora_fin, aul.bloque, aul.numero_aula, adh.dia " +
            "from persona person " +
            "INNER JOIN usuario us on person.id_persona = us.persona_id " +
            "INNER JOIN estudiante est on us.user_id = est.user_id " +
            "INNER JOIN estudiante_paralelo est_par on est.estudiante_id = est_par.estudiante_id " +
            "INNER JOIN paralelo par on est_par.paralelo_id = par.paralelo_id " +
            "INNER JOIN materia mat on par.materia_id = mat.materia_id " +
            "INNER JOIN docente docen on par.docente_docente_id = docen.docente_id " +
            "INNER JOIN usuario us_docen on docen.user_id = us_docen.user_id " +
            "INNER JOIN persona person_docen on us_docen.persona_id = person_docen.id_persona " +
            "INNER JOIN aula_dia_horario adh on par.paralelo_id = adh.paralelo_paralelo_id " +
            "INNER JOIN horario hor on adh.horario_id = hor.horario_id " +
            "INNER JOIN aula aul on adh.aula_id = aul.aula_id " +
            "WHERE us.user_id = #{userId} " +
            "AND est_par.estado = true " +
            "AND par.estado = true ")
    fun getSchedule(userId: Int): List<ScheduleEntity>
}