package com.example.project.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassMapper implements RowMapper<ClassModel> {
    @Override
    public ClassModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ClassModel(
                rs.getInt("id"),
                rs.getInt("numberofstudents"),
                rs.getFloat("classavg"),
                WhichClass.valueOf(rs.getString("whichclass")));
    }
}
