package com.example.project.controller;

import com.example.project.exceptions.*;
import com.example.project.model.*;
import com.example.project.service.ClassModelService;
import com.example.project.service.IRandomStudent;
import com.example.project.service.StudentModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("api/students")
@CrossOrigin
public class StudentController {

    @Autowired
    StudentModelService studentModelService;

    @Autowired
    ClassModelService classModelService;

    @Autowired
    private IRandomStudent iRandomStudentApi;

    @GetMapping
    public ResponseEntity getAllStudents() {
        try {
            List<StudentModel> result = studentModelService.getAllStudents();
            if (result != null) {
                return new ResponseEntity<List<StudentModel>>(result, HttpStatus.OK);
            }

        }
        catch ( ClientFaultExceptions e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getStudentById (@PathVariable Integer id) {
        try {

            StudentModel student = studentModelService.getStudentById(id);
            if (student != null) {
                return new ResponseEntity<StudentModel>(student, HttpStatus.OK);
            }
        }

        catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<String>("{ \"Warning\": \"not found student with that Id :" + id + "\" }",
                    HttpStatus.NOT_FOUND);
        }

        catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @PostMapping()
    public ResponseEntity createStudent(@RequestBody StudentModel student) {
        try {
            System.out.println(student);
            StudentModel studentToCreate = studentModelService.createStudent(student);
            if (studentToCreate != null) {
                return new ResponseEntity<String>(student.toString(), HttpStatus.CREATED);
            }
        }
        catch (ClientFaultExceptions e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.BAD_REQUEST);
        }

        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateStudent (@PathVariable Integer id, StudentModel student) {
        try {
            if (student != null) {
                studentModelService.updateStudent(student, id);
                return new ResponseEntity<StudentModel>(student, HttpStatus.OK);
            }
            throw new EmptyRequestException("could not find that classroom");
        }
        catch (ClientFaultExceptions e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteStudent (@PathVariable Integer id) {
        try{
            StudentModel studentToDelete = studentModelService.getStudentById(id);
            if (studentToDelete != null) {
                studentModelService.deleteStudent(id);
                return new ResponseEntity<String>(String.format("{ \"result\": \"student: %s has been deleted\" }", studentToDelete), HttpStatus.OK );
            }
            return new ResponseEntity<String>("{ \"Warning\": \"not found student with Id " + id + "\" }",
                    HttpStatus.NOT_FOUND);
        }

        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/byClassroomType")
    public ResponseEntity getAllStudentsByClassroomType(@RequestBody WhichClass whichClass)  {
        try {
            List<StudentModel> result = studentModelService.getAllStudentsByClassroomType(whichClass);
            if (result != null) {
                return new ResponseEntity<List<StudentModel>>(result, HttpStatus.OK);
            }

        }

        catch (ClientFaultExceptions e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }


    @GetMapping(value = "/getStudentsInClassroom/{id}")
    public ResponseEntity getStudentsInClassroom (@PathVariable Integer class_id) {
        try {
            ClassRoomDTO result = studentModelService.getStudentsInClassroom(class_id);
            if (result != null) {
                return new ResponseEntity<ClassRoomDTO>(result, HttpStatus.OK);
            }
        }
        catch (ClientFaultExceptions e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @PostMapping(value = "/create_random_student")
    public ResponseEntity createRandomStudent() {
        try{
            Random r = new Random();
            int min_grade = 40;
            int max_grade = 100;
            int rand_id = (int) (Math.random()*100+1);
            float rand_avgGrade = r.nextInt(max_grade-min_grade) + min_grade;
            int max_classes = classModelService.getAllClasses().size();
            int newStudentClassroomChoice = (int) (Math.random()*max_classes+1);
            RandomStudent randomStudent = iRandomStudentApi.getRandomStudent(rand_id);

            if (max_classes != 0 ) {
                StudentModel newStudent = new StudentModel(0, randomStudent.getRandom_s_lastName(), randomStudent.getRandom_s_firstName(),
                        rand_avgGrade, StudentGender.valueOf(randomStudent.getRandom_s_gender()), newStudentClassroomChoice);
                studentModelService.createStudent(newStudent);
                return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<String>("{ \"Error\": \"" + e.toString() + "\" }",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
      return null;
    }
}


