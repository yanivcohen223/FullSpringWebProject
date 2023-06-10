package com.example.project.service;

import com.example.project.model.RandomStudent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient (name = "randomStudentApi", url = "${randomStudentApi.url}")
public interface IRandomStudent {

    @GetMapping("/{id}")
    RandomStudent getRandomStudent(@PathVariable Integer id);

}
