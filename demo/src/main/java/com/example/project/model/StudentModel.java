package com.example.project.model;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class StudentModel {

    protected Integer id;

    protected String last_name;

    protected String first_name;

    protected float avgGrade;

    protected StudentGender gender;

    protected Integer class_id;


    public StudentModel(Integer id, String lastName, String firstName, float avgGrade, StudentGender gender, Integer class_id) {
        this.id = id;
        this.last_name = lastName;
        this.first_name = firstName;
        this.avgGrade = avgGrade;
        this.gender = gender;
        this.class_id = class_id;
    }

    @Override
    public String toString() {
        return "StudentModel{" +
                "id=" + id +
                ", lastName='" + last_name + '\'' +
                ", firstName='" + first_name + '\'' +
                ", avgGrade=" + avgGrade +
                ", gender=" + gender +
                ", class_id=" + class_id +
                '}';
    }
}
