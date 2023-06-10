package com.example.project.repository;

import com.example.project.exceptions.AllreadyExistsException;
import com.example.project.model.StudentModel;
import com.example.project.model.WhichClass;

import java.util.List;

public interface IStudentRepository {

    StudentModel createStudent (StudentModel student) throws AllreadyExistsException;

    void updateStudent (StudentModel student, Integer id);

    void deleteStudent (Integer id);

    List<StudentModel> getAllStudents();

    StudentModel getStudentById(Integer id);
    List<StudentModel> getAllStudentsByClassroomType(WhichClass whichClass);

    List<StudentModel> getStudentsInClassroom (Integer class_id);

}
