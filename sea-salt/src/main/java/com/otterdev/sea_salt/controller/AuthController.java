package com.otterdev.sea_salt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otterdev.sea_salt.dto.payload.OperationFail;
import com.otterdev.sea_salt.dto.payload.OperationSuccess;
import com.otterdev.sea_salt.dto.user.ReqRegisterDto;
import com.otterdev.sea_salt.dto.user.TokenDto;
import com.otterdev.sea_salt.service.UserService;
import com.otterdev.sea_salt.utility.Result;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Mono<ResponseEntity<Result<OperationSuccess, OperationFail>>> register(@RequestBody ReqRegisterDto dto) {
        return userService.register(dto)
                .map(result -> {
                    if (result.isOk()) {
                        return ResponseEntity.status(HttpStatus.CREATED)
                                .body(Result.<OperationSuccess, OperationFail>ok(
                                    new OperationSuccess("User registered successfully")
                                ));
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Result.<OperationSuccess, OperationFail>err(
                                    new OperationFail("Registration failed: " + result.unwrapErr().getMessage())
                                ));
                    }
                });
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Result<TokenDto, OperationFail>>> login(@RequestBody ReqRegisterDto dto) {
        return userService.login(dto.getEmail(), dto.getPassword())
                .map(result -> {
                    if (result.isOk()) {
                        return ResponseEntity.ok(Result.<TokenDto, OperationFail>ok(result.unwrap()));
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Result.<TokenDto, OperationFail>err(
                                    new OperationFail("Login failed: " + result.unwrapErr().getMessage())
                                ));
                    }
                });
    }

}// end class
