package com.example.project.repository;

import com.example.project.exceptions.AllreadyExistsException;
import com.example.project.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StudentRepository implements IStudentRepository{

    private static final String STUDENT_TABLE_NAME = "student";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;
    @Override
    public StudentModel createStudent(StudentModel student) throws AllreadyExistsException {
        try {
        /*String query = String.format("INSERT INTO %s (last_name, first_name, avgGrade, gender, class_id) VALUES (?,?,?,?,?)",STUDENT_TABLE_NAME);
        jdbcTemplate.update(query, student.getLast_name(), student.getFirst_name(), student.getAvgGrade(),student.getGender().name(), student.getClass_id());
        return student;*/
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);


        String queryNamedParam = String.format("INSERT INTO %s (last_name, first_name, avgGrade, gender, class_id) " +
                "VALUES (:last_name,:first_name,:avgGrade,:gender,:class_id)",STUDENT_TABLE_NAME);


        Map<String, Object> params = new HashMap<>();
        params.put("last_name", student.getLast_name());
        params.put("first_name", student.getFirst_name());
        params.put("avgGrade", student.getAvgGrade());
        params.put("gender", student.getGender().name());
        params.put("class_id", student.getClass_id());

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

        Integer id = (Integer) generatedKeyHolder.getKeys().get("id");

        System.out.println(student);
        student.setId(id);
        return student;

    }  catch (Exception e) {
        throw new AllreadyExistsException("There is not enough data to create a new class");
    }
    }

    @Override
    public void updateStudent(StudentModel student, Integer id) {
        String query = String.format("UPDATE %s SET last_name=?, first_name=?, avgGrade=?, gender=?, class_id=? WHERE id=?", STUDENT_TABLE_NAME);
        jdbcTemplate.update(query, student.getLast_name(), student.getFirst_name(), student.getAvgGrade(),student.getGender(), student.getClass_id(), id);

    }

    @Override
    public void deleteStudent(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id=?", STUDENT_TABLE_NAME);
        jdbcTemplate.update(query, id);

    }

    @Override
    public List<StudentModel> getAllStudents() {
        String query = String.format("Select * from %s;", STUDENT_TABLE_NAME);
        return jdbcTemplate.query(query, new StudentMapper());

    }

    @Override
    public StudentModel getStudentById(Integer id) {
        String query = "select * from student where id=1;";
        return jdbcTemplate.queryForObject(query, new StudentMapper());
    }

    @Override
    public List<StudentModel> getAllStudentsByClassroomType(WhichClass whichClass) {
        String query = String.format("SELECT * FROM %s WHERE class_id IN (" +
                "select id from %s WHERE whichClass= ?) ", STUDENT_TABLE_NAME, ClassRepository.CLASS_TABLE_NAME);
        return jdbcTemplate.query(query, new StudentMapper(), whichClass);
    }


    @Override
    public List<StudentModel> getStudentsInClassroom (Integer class_id) {
        String query = String.format("SELECT S.* FROM *s S" +
                "JOIN %s C" +
                "ON S.class_id = S.id" +
                "WHERE C.id =?", STUDENT_TABLE_NAME,ClassRepository.CLASS_TABLE_NAME);
        return jdbcTemplate.query(query, new StudentMapper(), class_id);
    }
}
