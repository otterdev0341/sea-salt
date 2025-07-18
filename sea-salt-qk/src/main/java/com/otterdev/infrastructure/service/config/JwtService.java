package com.otterdev.infrastructure.service.config;



import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.otterdev.domain.valueObject.jwt.JwtClaimDto;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class JwtService {
    
    @Inject
    JsonWebToken jwt;
    
    public String generateJwt(JwtClaimDto payload) {
        
        return Jwt.issuer("sea-salt")
                .subject(payload.getSubject())
                .groups(payload.getGroups())
                .expiresIn(payload.getExpiresInMillis())
                .sign();
    }
    
    /**
     * Extracts the current user ID from the JWT token
     * @return Optional containing the user ID if valid, empty if invalid or not present
     */
    public Optional<UUID> getCurrentUserId() {
        try {
            if (jwt == null || jwt.getSubject() == null) {
                return Optional.empty();
            }
            
            String userIdString = jwt.getSubject();
            UUID userId = UUID.fromString(userIdString);
            return Optional.of(userId);
        } catch (IllegalArgumentException | NullPointerException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Gets the current user's roles/groups from JWT
     * @return Optional containing the groups set, empty if not present
     */
    public Optional<java.util.Set<String>> getCurrentUserGroups() {
        try {
            if (jwt == null || jwt.getGroups() == null) {
                return Optional.empty();
            }
            return Optional.of(jwt.getGroups());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Validates if current user has a specific role
     * @param role the role to check
     * @return true if user has the role, false otherwise
     */
    public boolean hasRole(String role) {
        return getCurrentUserGroups()
            .map(groups -> groups.contains(role))
            .orElse(false);
    }
}
