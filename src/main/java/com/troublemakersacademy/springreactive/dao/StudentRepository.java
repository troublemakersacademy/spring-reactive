package com.troublemakersacademy.springreactive.dao;

import com.troublemakersacademy.springreactive.entity.Grade;
import com.troublemakersacademy.springreactive.entity.Student;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface StudentRepository extends ReactiveMongoRepository<Student,String> {
    Flux<Student> findByGrade(Grade grade);
    Flux<Student> findByLastName(String lastName);
}
