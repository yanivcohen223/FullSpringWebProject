package com.example.project.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentMapper implements RowMapper<StudentModel> {

    @Override
    public StudentModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new StudentModel(
                rs.getInt("id"),
                rs.getString("last_name"),
                rs.getString("first_name"),
                rs.getFloat("avggrade"),
                StudentGender.valueOf(rs.getString("gender")),
                rs.getInt("class_id")

        );
    }
}
