package com.example.project.controller;

import com.example.project.exceptions.ClassRoomIsNotExistsException;
import com.example.project.exceptions.ClientFaultExceptions;
import com.example.project.exceptions.EmptyRequestException;
import com.example.project.exceptions.WrongParamsException;
import com.example.project.model.ClassModel;
import com.example.project.service.ClassModelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/class")
@CrossOrigin
public class ClassController {

    @Autowired
    ClassModelService classModelService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity getAll(){
        try {
            List<ClassModel> result = classModelService.getAllClasses();
            return new ResponseEntity<List<ClassModel>>(result, HttpStatus.OK);
        }
        catch (ClientFaultExceptions e){
            return new ResponseEntity<String>("{ \"Error\": \"" + e.getMessage() + "\" }",
                    HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }}

    @GetMapping("/{id}")
    public ResponseEntity getById (@PathVariable Integer id) {
        try {
            ClassModel result = classModelService.getClassById(id);
            if (result != null) {
                return new ResponseEntity<ClassModel>(result, HttpStatus.OK);
            }

        }
        catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<String>("{ \"Warning\": \"not found Class with that Id :" + id + "\" }",
                    HttpStatus.NOT_FOUND);
        }

        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @PostMapping
    public ResponseEntity postClassRoom(@RequestBody ClassModel classroom) throws JsonProcessingException {
        try {
            String json = objectMapper.writeValueAsString(classroom);
            ClassModel classModel = objectMapper.readValue(json, ClassModel.class);
            String result = classModelService.createClass(classroom);
            if ( result != null) {
                return new ResponseEntity<String>("{ \"Result\": Class Created }", HttpStatus.CREATED);
            }

        }
        catch (ClassRoomIsNotExistsException e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.BAD_REQUEST);
        }

        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<String> updateClass (@PathVariable Integer id, @RequestBody ClassModel classroom) {
        try {
            if (classroom != null) {

                classModelService.updateClass(classroom, id);
                return new ResponseEntity<String>("{ \"result\": \"class has been updated\" }", HttpStatus.OK);
            }
            else {
                throw new EmptyRequestException("could not find that classroom");
            }

        }
        catch ( ClientFaultExceptions e) {

            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {

            return new ResponseEntity<String>(String.format("{ Error: '%s' }", e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteClass(@PathVariable Integer id) throws ClientFaultExceptions {
        try {
            ClassModel classRoomToDelete = classModelService.getClassById(id);
            if (classRoomToDelete != null) {
                classModelService.deleteClass(id);
                return new ResponseEntity<String>("{ \"result\": \"class has been deleted\" }", HttpStatus.OK);
            }
            else {
                throw new WrongParamsException("cannot find classroom with that kind of id");
            }
        }
        catch (ClientFaultExceptions e){
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>(String.format("{ Error: '%s' }", e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
