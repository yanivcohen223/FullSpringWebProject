package com.example.project.repository;

import com.example.project.exceptions.AllreadyExistsException;
import com.example.project.model.ClassModel;
import com.example.project.model.WhichClass;

import java.util.List;

public interface IClassRepository {

    ClassModel createClass (ClassModel classModel) throws AllreadyExistsException;

    void updateClass (ClassModel classModel, Integer id);

    void updateClassAvg_numberOfStudents(Integer id);

    void deleteClass (Integer id);

    List<ClassModel> getAllClasses();

    ClassModel getClassById(Integer id);

    List<ClassModel> getClassByTypeClass(WhichClass whichClass);
}
