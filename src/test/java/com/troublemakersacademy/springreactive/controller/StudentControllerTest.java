package com.troublemakersacademy.springreactive.controller;

import com.troublemakersacademy.springreactive.dao.StudentRepository;
import com.troublemakersacademy.springreactive.entity.Grade;
import com.troublemakersacademy.springreactive.entity.Student;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private StudentRepository studentRepository;

    @Before
    public void setUp(){
    studentRepository.deleteAll()
    .thenMany(Flux.fromIterable(getStudents()))
    .flatMap(studentRepository :: save)
    .then()
     .block();

    }

    private List<Student> getStudents(){
        List<Student> list = new ArrayList<>();
        list.add(new Student("Jhon","Ken", Grade.III));
        list.add(new Student("Raj","Jain", Grade.II));
        list.add(new Student("Samir","Gupta", Grade.X));
        return  list;
    }

    @Test
    public void getAllStudentTest(){
        client.get().uri("/students")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Student.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void getStudentTest(){
        client.get().uri("/students/5ed386fd15e3282db792e62c")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Student.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void createStudentTest(){
        client.post().uri("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(getStudent() ),Student.class )
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.studentId").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo("Junit First Name")
                .consumeWith(System.out::println);
    }

    private Student getStudent() {
        Student student = new Student();
        student.setGrade(Grade.IX);
        student.setFirstName("Junit First Name");
        student.setLastName("Last name");
        return student;
    }
}
