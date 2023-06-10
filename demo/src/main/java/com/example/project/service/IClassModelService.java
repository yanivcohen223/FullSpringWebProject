package com.example.project.service;

import com.example.project.exceptions.*;
import com.example.project.model.ClassModel;
import com.example.project.model.WhichClass;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IClassModelService {

    String createClass (ClassModel classModel) throws ClassRoomIsNotExistsException, AllreadyExistsException;

    String updateClass (ClassModel classModel, Integer id) throws JsonProcessingException, AllreadyExistsException;

    String deleteClass (Integer id) throws UnAuthorisedActionException;

    List<ClassModel> getAllClasses() throws NoAvailableClassesException;

    ClassModel getClassById(Integer id) throws EmptyRequestException;

    List<ClassModel> getClassByTypeClass(WhichClass whichClass) throws NoAvailableClassesException;
}
