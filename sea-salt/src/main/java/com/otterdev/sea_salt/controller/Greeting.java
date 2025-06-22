package com.otterdev.sea_salt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/greeting")
public class Greeting {
    
    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Hello, World!");
    }

    @GetMapping("/goodbye")
    public Mono<String> goodbye() {
        return Mono.just("Goodbye, World!");
    }
}
