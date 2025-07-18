package com.otterdev.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Basic(fetch = FetchType.EAGER)
    @Column(name= "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name= "password")
    private String password;

    @Column(name= "username")
    private String username;


    @Column(name= "first_name")
    private String firstName;

    
    @Column(name= "last_name")
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "gender",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(
            name = "fk_users_gender",
            foreignKeyDefinition = 
                "FOREIGN KEY (gender) REFERENCES genders(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private Gender gender;  

    @Column(name= "dob")
    private LocalDateTime dob;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "role",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(
            name = "fk_users_role",
            foreignKeyDefinition = 
                "FOREIGN KEY (role) REFERENCES roles(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private Role role;

    @JsonIgnore
    @Column(name= "created_at")
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column(name= "updated_at")
    private LocalDateTime updatedAt;

     

}
