package com.otterdev.controller;

import java.util.Optional;
import java.util.UUID;
import com.otterdev.dto.entity.property.ReqCreatePropertyDto;
import com.otterdev.dto.entity.property.ReqUpdateProperty;
import com.otterdev.dto.helper.response.ErrorResponse;
import com.otterdev.dto.helper.response.SuccessResponse;
import com.otterdev.entity.table.Property;
import com.otterdev.error.service.ServiceError;
import com.otterdev.service.PropertyService;
import com.otterdev.service.JwtService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/properties")
@ApplicationScoped
public class PropertyController {
    
    @Inject
    private PropertyService propertyService;

    @Inject
    private JwtService jwtService;

    @POST
    @Path("/")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createProperty(@Valid ReqCreatePropertyDto propertyDto) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        // Call the service to create a new property
        return propertyService.newProperty(propertyDto, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Property Creation Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                Property newProperty = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Property created successfully", newProperty);
                return Response.status(Response.Status.CREATED)
                    .entity(successResponse)
                    .build();
            });
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateProperty(@PathParam("id") UUID propertyId, @Valid ReqUpdateProperty propertyDto) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return propertyService.editProperty(propertyId, propertyDto, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Property Update Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                Property updatedProperty = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Property updated successfully", updatedProperty);
                return Response.ok(successResponse).build();
            });
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteProperty(@PathParam("id") UUID propertyId) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return propertyService.deleteProperty(propertyId, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Property Deletion Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                SuccessResponse successResponse = new SuccessResponse("Property deleted successfully", null);
                return Response.ok(successResponse).build();
            });
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getPropertyById(@PathParam("id") UUID propertyId) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return propertyService.viewPropertyById(propertyId, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Property Retrieval Failed", error.message());
                    return Response.status(Response.Status.NOT_FOUND)
                        .entity(errorResponse)
                        .build();
                }
                
                Property property = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Property retrieved successfully", property);
                return Response.ok(successResponse).build();
            });
    }

    @GET
    @Path("/")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getUserProperties() {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return propertyService.viewAllUserProperties(userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Properties Retrieval Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                SuccessResponse successResponse = new SuccessResponse("Properties retrieved successfully", result.getRight());
                return Response.ok(successResponse).build();
            });
    }

    @GET
    @Path("/by-status/{statusId}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getPropertiesByStatus(@PathParam("statusId") UUID statusId) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return propertyService.viewPropertiesByStatus(statusId, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Properties by Status Retrieval Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                SuccessResponse successResponse = new SuccessResponse("Properties by status retrieved successfully", result.getRight());
                return Response.ok(successResponse).build();
            });
    }

} // end class
