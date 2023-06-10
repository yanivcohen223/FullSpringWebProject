package com.example.project.service;

import com.example.project.exceptions.*;
import com.example.project.model.ClassRoomDTO;
import com.example.project.model.StudentModel;
import com.example.project.model.WhichClass;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IStudentsModelService {

    StudentModel createStudent(StudentModel student) throws FullClassException, EmptyRequestException, WrongParamsException, JsonProcessingException, AllreadyExistsException;

    void updateStudent (StudentModel student, Integer id) throws EmptyRequestException, JsonProcessingException;

    String deleteStudent (Integer id) throws EmptyRequestException, JsonProcessingException;

    List<StudentModel> getAllStudents() throws EmptyRequestException;

    StudentModel getStudentById(Integer id) throws WrongParamsException;

    List<StudentModel> getAllStudentsByClassroomType(WhichClass whichClass) throws ClassRoomIsNotExistsException, ClassroomTypeIsNotExistsException;

    ClassRoomDTO getStudentsInClassroom (Integer class_id) throws ClassroomTypeIsNotExistsException, EmptyClassroomException, ClassRoomIsNotExistsException;

}
