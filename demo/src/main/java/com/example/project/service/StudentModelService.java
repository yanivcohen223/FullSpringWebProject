package com.example.project.service;

import com.example.project.exceptions.*;
import com.example.project.model.ClassModel;
import com.example.project.model.ClassRoomDTO;
import com.example.project.model.StudentModel;
import com.example.project.model.WhichClass;
import com.example.project.repository.CacheRepositoryImpl;
import com.example.project.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentModelService implements IStudentsModelService{

    @Autowired
    protected StudentRepository studentRepository;

    @Autowired
    private CacheRepositoryImpl cacheRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected ClassModelService classModelService;

    @Value("${maxStudentInExternalClass}")
    private Integer maxStudentInExternalClass;

    @Value("${cache_on}")
    private Boolean cache_on;


    @Override
    public StudentModel createStudent(StudentModel student) throws FullClassException, EmptyRequestException, WrongParamsException, JsonProcessingException, AllreadyExistsException {

        if (student.getAvgGrade() > 100 || student.getAvgGrade() < 0 ) {
            System.out.println(student.getAvgGrade());
            throw new WrongParamsException("avg grade is illigeal");
        }

        //getting the student class by the given id
        ClassModel studentClass = classModelService.getClassById(student.getClass_id());
        System.out.println(studentClass);

        //checking if there is more than 20 students in the classroom if so throwing error
        if (studentClass.getWhichclass() == WhichClass.External && maxStudentInExternalClass == studentClass.getNumberofstudents()) {
            throw new FullClassException("{\"Warning\": \"Cannot Add more students to this class\" }");
            //need to create new exception to full class exception
        }

        //creating new student
        StudentModel studentToCreate = studentRepository.createStudent(student);
        System.out.println(studentToCreate);

        //updating the class
        classModelService.classRepository.updateClassAvg_numberOfStudents(studentToCreate.getClass_id());

        return studentToCreate;
    }

    @Override
    public void updateStudent(StudentModel student, Integer id) throws EmptyRequestException, JsonProcessingException {

        //updating the student
        studentRepository.updateStudent(student, id);

        //updating the cache
        if (cache_on && cacheRepository.isKeyExist(String.valueOf(String.format("student id: %d",id)))) {
            cacheRepository.updateCacheEntity(String.valueOf(String.format("student id: %d",id)), objectMapper.writeValueAsString(student));
        }

        //updating the class
        classModelService.classRepository.updateClassAvg_numberOfStudents(student.getClass_id());

    }

    @Override
    public String deleteStudent(Integer id) throws EmptyRequestException, JsonProcessingException {
        try{
            //deleting the student
            StudentModel studentToDelete = studentRepository.getStudentById(id);
            if (studentToDelete != null) {
                //updating the class
                classModelService.classRepository.updateClassAvg_numberOfStudents(studentToDelete.getClass_id());

                studentRepository.deleteStudent(studentToDelete.getId());

                if (cache_on && cacheRepository.isKeyExist(String.valueOf(String.format("student id: %d",id)))) {
                    cacheRepository.removeKey(String.valueOf(String.format("student id: %d",id)));
                }

                return "student deleted successfully";
            }

        }
        catch (Exception e){
            throw e;
        }
        return null;
    }

    //getting all the students
    @Override
    public List<StudentModel> getAllStudents() throws EmptyRequestException {
        List<StudentModel> result = studentRepository.getAllStudents();
        if (result.isEmpty()) {
            throw new EmptyRequestException("No students to Desplay");
        }
        return result;
    }

    //getting the student by id
    @Override
    public StudentModel getStudentById(Integer id) throws WrongParamsException {
        try {;
        if (cache_on && cacheRepository.isKeyExist(String.valueOf(String.format("student id: %d",id)))) {
            String student = cacheRepository.getCacheEntity(String.valueOf(String.format("student id: %d",id)));
            System.out.println("reading from cache " + student);
            return objectMapper.readValue(student, StudentModel.class);
        }

        StudentModel result = studentRepository.getStudentById(id);

        if (cache_on) {
            cacheRepository.createCacheEntity(String.valueOf(String.format("student id: %d",id)), objectMapper.writeValueAsString(result));
        }
        return result;


    } catch (JsonProcessingException e) {
        System.out.println(e);
        throw new IllegalStateException("cannot write json of customer");
    }
    }

    @Override
    public List<StudentModel> getAllStudentsByClassroomType(WhichClass whichClass) throws ClassroomTypeIsNotExistsException {
        List<StudentModel> result = studentRepository.getAllStudentsByClassroomType(WhichClass.valueOf(whichClass.name()));

        if (result != null) {
            return result;
        }
        throw new ClassroomTypeIsNotExistsException(String.format("there is no class with type : %s",WhichClass.valueOf(whichClass.name())));
    }

    @Override
    public ClassRoomDTO getStudentsInClassroom(Integer class_id) throws ClassroomTypeIsNotExistsException, EmptyClassroomException, ClassRoomIsNotExistsException {
        ClassModel selectedClass = classModelService.getClassById(class_id);
        List<StudentModel> selectedStudentList = studentRepository.getStudentsInClassroom(class_id);
        ClassRoomDTO newClassRoomDTO = new ClassRoomDTO(selectedClass, selectedStudentList);
        if (newClassRoomDTO.getClassroomDto() == null) {
            throw new ClassRoomIsNotExistsException("classroom does not exists");
        } else if (newClassRoomDTO.getStudentModelList().size() == 0) {
            throw new EmptyClassroomException("there is no students in this class");
        }
        return newClassRoomDTO;
    }

}
