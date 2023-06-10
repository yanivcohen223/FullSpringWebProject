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
public class ClassRepository implements IClassRepository{

    protected static final String CLASS_TABLE_NAME = "school_classes";

    @Autowired
    protected StudentRepository studentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;
    @Override
    public ClassModel createClass(ClassModel classModel) throws AllreadyExistsException {
        try {
        /*String query = String.format("INSERT INTO %s (numberOfStudents, classAvg, whichClass) VALUES (?, ?, ?)", CLASS_TABLE_NAME);
        jdbcTemplate.update(query, classModel.getNumberofstudents(), classModel.getClassavg(), classModel.getWhichclass().name());
        return classModel;*/

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String queryNamedParam = String.format("INSERT INTO %s (numberofstudents, classavg, whichclass) VALUES (:numberofstudents, :classavg, :whichclass)", CLASS_TABLE_NAME);


        Map<String, Object> params = new HashMap<>();
        params.put("numberofstudents", classModel.getNumberofstudents());
        params.put("classavg", classModel.getClassavg());
        params.put("whichclass", classModel.getWhichclass().name());

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

        Integer id = (Integer) generatedKeyHolder.getKeys().get("id");

            System.out.println(classModel);
        classModel.setId(id);
        return classModel;

    }  catch (Exception e) {
        throw new AllreadyExistsException("There is not enough data to create a new class");
    }
    }

    @Override
    public void updateClass(ClassModel classModel, Integer id) {
        String query = String.format("UPDATE %s SET numberOfStudents=?, classAvg=?, whichClass=? WHERE id=?", CLASS_TABLE_NAME);
        jdbcTemplate.update(query, classModel.getNumberofstudents(), classModel.getClassavg(), classModel.getWhichclass().name(), id);
    }

    @Override
    public void updateClassAvg_numberOfStudents(Integer id) {
        String query = String.format("UPDATE %s SET classAvg= (Select avg(avgGrade) from student where class_id = ?)," +
                " numberOfStudents= (Select count(*) from student where class_id = ?)  WHERE id= ?", CLASS_TABLE_NAME);
        jdbcTemplate.update(query, id, id, id);
    }

    @Override
    public void deleteClass(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id=?", CLASS_TABLE_NAME);
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<ClassModel> getAllClasses() {
        String query = String.format("SELECT * FROM %s", CLASS_TABLE_NAME);
        return jdbcTemplate.query(query, new ClassMapper());
    }

    @Override
    public ClassModel getClassById(Integer id) {
        String query = String.format("SELECT * FROM %s WHERE id =?", CLASS_TABLE_NAME);
        return jdbcTemplate.queryForObject(query, new ClassMapper(), id);
    }

    @Override
    public List<ClassModel> getClassByTypeClass(WhichClass whichClass) {
        String query = String.format("SELECT * FROM %s WHERE whichClass=?",CLASS_TABLE_NAME);
        return jdbcTemplate.query(query, new ClassMapper(), whichClass);
    }


}
