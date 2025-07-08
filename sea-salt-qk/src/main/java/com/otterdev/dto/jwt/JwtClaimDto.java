package com.otterdev.dto.jwt;

import java.util.Set;

import lombok.Data;

@Data
public class JwtClaimDto {
    private String subject;
    private Set<String> groups;
    private Long expiresInMillis;
}
