package com.otterdev.dto.entity.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDetailDto {
    private UUID id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dob;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
