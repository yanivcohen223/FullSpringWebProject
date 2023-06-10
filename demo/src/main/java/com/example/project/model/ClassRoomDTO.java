package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ClassRoomDTO {

    protected ClassModel classroomDto;

    protected List<StudentModel> studentModelList;

    public ClassRoomDTO(ClassModel classroomDto, List<StudentModel> studentModelList) {
        this.classroomDto = classroomDto;
        this.studentModelList = studentModelList;
    }

    public ClassRoomDTO() {
    }



    @Override
    public String toString() {
        return "ClassRoomDTO{" +
                "classroomDto=" + classroomDto +
                ", studentModelList=" + studentModelList +
                '}';
    }
}
