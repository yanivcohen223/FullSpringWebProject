package com.example.project.service;

import com.example.project.exceptions.AllreadyExistsException;
import com.example.project.exceptions.ClassRoomIsNotExistsException;
import com.example.project.exceptions.NoAvailableClassesException;
import com.example.project.exceptions.UnAuthorisedActionException;
import com.example.project.model.ClassModel;
import com.example.project.model.StudentModel;
import com.example.project.model.WhichClass;
import com.example.project.repository.CacheRepositoryImpl;
import com.example.project.repository.ClassRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class ClassModelService implements IClassModelService{


    @Autowired
    protected ClassRepository classRepository;

    @Autowired
    private CacheRepositoryImpl cacheRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${cache_on}")
    private Boolean cache_on;


    @Override
    public String createClass(ClassModel classModel) throws ClassRoomIsNotExistsException, AllreadyExistsException {
        ClassModel classToCreate = classRepository.createClass(classModel);
        if (classToCreate != null) {
            return classToCreate.toString();
        }
        throw new ClassRoomIsNotExistsException("classroom already exists");
    }

    @Override
    public String updateClass(ClassModel classModel, Integer id) throws JsonProcessingException, AllreadyExistsException {
        try{
            ClassModel classToUpdate = classRepository.getClassById(id);
            if (classToUpdate != null){
                classRepository.updateClass(classModel, id);
            }
            if (cache_on && cacheRepository.isKeyExist(String.valueOf(String.format("class id: %d",id)))) {
                cacheRepository.updateCacheEntity(String.valueOf(String.format("class id: %d",id)), objectMapper.writeValueAsString(classToUpdate));
            }
            return "classroom has been updated";

        }
        catch (EmptyResultDataAccessException e) {
            classRepository.createClass(classModel);
            return "Didn't find a class to update so created for you a new one";
        }
    }

    @Override
    public String deleteClass(Integer id) throws UnAuthorisedActionException {
        ClassModel currentClassroom = classRepository.getClassById(id);
        if (currentClassroom.getNumberofstudents() > 0) {
            throw new UnAuthorisedActionException("{\"Warning\": \"Cannot Delete class with students\" }");
        }

        classRepository.deleteClass(currentClassroom.getId());

        if (cache_on && cacheRepository.isKeyExist(String.valueOf(id))) {
            cacheRepository.removeKey(String.valueOf(String.format("class id: %d",id)));
        }
        return "class deleted successfully";


    }

    @Override
    public List<ClassModel> getAllClasses() throws NoAvailableClassesException {
        List<ClassModel> result = classRepository.getAllClasses();
        if (result.isEmpty()){
            throw new NoAvailableClassesException("there are no classes to display please create a class");
        }
        return result;
    }

    @Override
    public ClassModel getClassById(Integer id) {

        try {;
            if (cache_on && cacheRepository.isKeyExist(String.valueOf(id))) {
                String classroom = cacheRepository.getCacheEntity(String.valueOf(String.format("class id: %d",id)));
                System.out.println("reading from cache " + classroom);
                return objectMapper.readValue(classroom, ClassModel.class);
            }

            ClassModel result = classRepository.getClassById(id);

            if (cache_on) {
                cacheRepository.createCacheEntity(String.valueOf(String.format("class id: %d",id)), objectMapper.writeValueAsString(result));
            }
            return result;



        } catch (JsonProcessingException e) {
            System.out.println(e);
            throw new IllegalStateException("cannot write json of class");
        }
    }

    @Override
    public List<ClassModel> getClassByTypeClass(WhichClass whichClass) throws NoAvailableClassesException {
        List<ClassModel> result = classRepository.getClassByTypeClass(whichClass);
        if (result.isEmpty()){
            throw new NoAvailableClassesException("there are no External Classrooms to display please create a classroom");
        }
        return result;
    }

}
