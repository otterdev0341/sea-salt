package com.otterdev.application.handler.test;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("test")
public class TestLocalDateTimeHandler {
    
    @POST
    @Path("localDateTime")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> testLocalDateTime(@Valid TestLocalTestTimeDto dto) {
        
        return Uni.createFrom().item(() -> {
            // Simulate processing the LocalDateTime
            
            return Response.ok("LocalDateTime processed successfully: " + dto.getIncommingLocalDateTime()).build();
        });
        
    }

}
