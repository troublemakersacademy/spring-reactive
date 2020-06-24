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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentRouterHandlerTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private StudentRepository repository;

    private List<Student> getStudents(){
        List<Student> list = new ArrayList<>();
        list.add(new Student("Jhon","Ken", Grade.III));
        list.add(new Student("Raj","Jain", Grade.II));
        list.add(new Student("Samir","Gupta", Grade.X));
        return  list;
    }

    @Before
    public  void setUp(){
        repository.deleteAll()
        .thenMany(Flux.fromIterable(getStudents()))
        .flatMap(student -> {return  repository.save(student);})
        .doOnEach(System.out::println)
        .then()
        .block();

    }

    @Test
    public void getAllStudentTest(){
        client.get().uri("/studentRoute")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Student.class)
                .consumeWith(System.out::println);
    }


    @Test
    public void createStudentTest(){
        Student student  = new Student("Jhon","Ken", Grade.III);
        client.post().uri("/studentRoute")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(student ),Student.class )
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.studentId").isNotEmpty()
                .jsonPath("$.firstName").isNotEmpty()
                .consumeWith(System.out::println);
    }
    @Test
    public void getStudentTest(){
        Student student  = new Student("Jhon","Ken", Grade.III);
        client.get().uri("/studentRoute/{id}",student.getStudentId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(response -> assertThat(response.getResponseBody()).isNotNull());
    }

}
