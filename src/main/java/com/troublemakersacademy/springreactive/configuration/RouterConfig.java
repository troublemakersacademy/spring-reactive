package com.troublemakersacademy.springreactive.configuration;

import com.troublemakersacademy.springreactive.handler.StudentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> route(StudentHandler studentHandler){
        return RouterFunctions.route(
                GET("/studentRoute/{id}").and(accept(APPLICATION_JSON)),studentHandler::getStudent)
                .andRoute(GET("/studentRoute").and(accept(APPLICATION_JSON)), studentHandler:: getAllStudents)
                .andRoute(POST("/studentRoute").and(accept(APPLICATION_JSON)),studentHandler::createStudent);
    }
}
