package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ClassModel {
    @Getter @Setter
    protected Integer id;
    @Getter @Setter
    protected Integer numberofstudents;
    @Getter @Setter
    protected float classavg;
    @Getter @Setter
    protected WhichClass whichclass;


    public ClassModel(Integer id, Integer numberofstudents, float classavg, WhichClass whichclass) {
        this.id = id;
        this.numberofstudents = numberofstudents;
        this.classavg = classavg;
        this.whichclass = whichclass;
    }


    @Override
    public String toString() {
        return "ClassModel{" +
                "id=" + id +
                ", numberOfStudents=" + numberofstudents +
                ", classAvg=" + classavg +
                ", whichClass=" + whichclass +
                 '}';
    }


}
