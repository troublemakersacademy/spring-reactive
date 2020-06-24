package com.troublemakersacademy.springreactive.handler;

import com.troublemakersacademy.springreactive.dao.StudentRepository;
import com.troublemakersacademy.springreactive.entity.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class StudentHandler {

    private StudentRepository repository;

    public  StudentHandler(StudentRepository studentRepository){
        this.repository = studentRepository;
    }

    public Mono<ServerResponse> createStudent(ServerRequest serverRequest){
        Mono<Student> studentMono = serverRequest.bodyToMono(Student.class);
        return  studentMono.flatMap(student ->
                 ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(repository.save(student),Student.class) );

    }


    public  Mono<ServerResponse> getStudent(ServerRequest serverRequest){
        String studentId =serverRequest.pathVariable("id");
        Mono<Student> studentMono = repository.findById(studentId);
        return studentMono.flatMap(student -> ServerResponse.ok()
                .body(fromValue(student)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }


    public  Mono<ServerResponse> getAllStudents(ServerRequest serverRequest){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(repository.findAll(),Student.class);
    }



}
