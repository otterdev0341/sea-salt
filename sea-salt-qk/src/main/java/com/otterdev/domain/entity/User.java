package com.otterdev.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @UniqueElements
    @Column(name= "email")
    private String email;

    @JsonIgnore
    @Column(name= "password")
    private String password;

    @Column(name= "username")
    private String username;

    @JsonIgnore
    @Column(name= "first_name")
    private String firstName;

    @JsonIgnore
    @Column(name= "last_name")
    private String lastName;

    @JsonIgnore
    @Column(name= "gender")
    private UUID gender;  // Assuming gender is stored as UUID (e.g. gender lookup table)

    @JsonIgnore
    @Column(name= "dob")
    private LocalDate dob;

    @Column(name= "role")
    private String role;

    @JsonIgnore
    @Column(name= "created_at")
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column(name= "updated_at")
    private LocalDateTime updatedAt;

     

}
