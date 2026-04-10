package com.reactive.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class MoviesController {

    @GetMapping
    public Mono<?> getHello(){
        return Mono.just("Hi there");
    }
}
