package com.troublemakersacademy.springreactive.controller;

import com.troublemakersacademy.springreactive.dao.StudentRepository;
import com.troublemakersacademy.springreactive.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Student> createStudent(@RequestBody Student student){
        Mono<Student> studentMono = studentRepository.save(student);
        return studentMono;
    }

    @GetMapping("{studentId}")
    public  Mono<Student> getStudent(@PathVariable String studentId){
        return studentRepository.findById(studentId);
    }


    /**
     * Returns all student data
     * @return
     */
    @GetMapping()
    public Flux<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    /**
     * Update a existing Student data
     * @param student
     * @param studentId
     * @return
     */
    @PutMapping("{studentId}")
    public Mono<ResponseEntity<Student>> updateStudent(@RequestBody Student student,@PathVariable String studentId){
      return   studentRepository.findById(studentId)
                .flatMap(existingStudent -> {
                    existingStudent.setFirstName(student.getFirstName()) ;
                    existingStudent.setLastName(student.getLastName()) ;
                    existingStudent.setGrade(student.getGrade());
                    return studentRepository.save(existingStudent);
                }).map(updatedStudent -> new ResponseEntity<Student>( updatedStudent,HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Deletes a Student Record from Mongo
     * Returns default a default value is not found
     * @param studentId
     * @return
     */
    @RequestMapping(value = {"studentId"},method = RequestMethod.DELETE)
    public  Mono<ResponseEntity<Void>> deleteStudent(@PathVariable String studentId){
        return studentRepository.deleteById(studentId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
