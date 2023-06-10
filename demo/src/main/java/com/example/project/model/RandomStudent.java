package com.example.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RandomStudent {

    @JsonProperty("lastName")
    protected String random_s_lastName;

    @JsonProperty("firstName")
    protected String random_s_firstName;

    @JsonProperty("gender")
    protected String random_s_gender;

    public RandomStudent() {
    }

    public RandomStudent(String random_s_lastName, String random_s_firstName, String random_s_gender) {
        this.random_s_lastName = random_s_lastName;
        this.random_s_firstName = random_s_firstName;
        this.random_s_gender = random_s_gender;
    }

    @Override
    public String toString() {
        return "RandomStudent{" +
                "random_s_lastName='" + random_s_lastName + '\'' +
                ", random_s_firstName='" + random_s_firstName + '\'' +
                ", random_s_gender='" + random_s_gender + '\'' +
                '}';
    }
}
