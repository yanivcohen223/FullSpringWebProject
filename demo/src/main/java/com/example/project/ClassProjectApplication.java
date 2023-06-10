package com.example.project;

import com.example.project.exceptions.AllreadyExistsException;
import com.example.project.model.ClassModel;
import com.example.project.model.StudentGender;
import com.example.project.model.StudentModel;
import com.example.project.model.WhichClass;
import com.example.project.repository.ClassRepository;
import com.example.project.repository.StudentRepository;
import com.example.project.service.RedisDetailsConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(value = {RedisDetailsConfig.class})
public class ClassProjectApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ClassProjectApplication.class, args);
		System.out.println("hello world");


	}

	@Bean
	CommandLineRunner commandLineRunner (JdbcTemplate jdbcTemplate, ClassRepository classRepository, StudentRepository studentRepository ) throws AllreadyExistsException {
		jdbcTemplate.execute(
				"DROP TABLE IF EXISTS student cascade;\n" +
						"DROP TABLE IF EXISTS school_classes cascade;\n" +
						"CREATE TABLE school_classes (" +
						"    id SERIAL PRIMARY KEY,\n" +
						"    numberofstudents int NOT NULL default 0,\n" +
						"    classavg float NOT NULL default 0,\n" +
						"    whichclass varchar(255) NOT NULL default '');\n"+

						"CREATE TABLE student ("+
						"    id SERIAL PRIMARY KEY,\n" +
						"    last_name varchar(255) NOT NULL default '',\n" +
						"    first_name varchar(255) NOT NULL default '',\n" +
						"    avgGrade float NOT NULL default 0,\n" +
						"    gender varchar(255) NOT NULL default '',\n" +
						"    class_id int NOT NULL default 0,\n" +
						"    FOREIGN KEY(class_id) REFERENCES school_classes(id));");


		classRepository.createClass(new ClassModel(0,0,0, WhichClass.valueOf("External")));
		studentRepository.createStudent(new StudentModel(0,"cohen", "yaniv", 97.5f, StudentGender.male, 1));

		return args -> {
		};
	}

}
