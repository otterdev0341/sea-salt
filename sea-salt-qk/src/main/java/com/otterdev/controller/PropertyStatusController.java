package com.otterdev.controller;

import java.util.Optional;

import java.util.UUID;

import com.otterdev.dto.entity.propertyStatus.ReqCreateUpdatePropertyStatusDto;
import com.otterdev.dto.helper.response.ErrorResponse;
import com.otterdev.dto.helper.response.SuccessResponse;
import com.otterdev.entity.table.PropertyStatus;
import com.otterdev.error.service.ServiceError;
import com.otterdev.service.JwtService;
import com.otterdev.service.PropertyStatusService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/property-status")
@ApplicationScoped
public class PropertyStatusController {
    
    @Inject
    JwtService jwtService;

    @Inject
    PropertyStatusService propertyStatusService;

    @POST
    @Path("/")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createPropertyStatus(@Valid ReqCreateUpdatePropertyStatusDto propertyStatusDto) {
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();

        // Call the service to create a new property status
        return propertyStatusService.newPropertyStatus(propertyStatusDto, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Creation Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }

                PropertyStatus newPropertyStatus = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Property status created successfully", newPropertyStatus);
                return Response.status(Response.Status.CREATED)
                    .entity(successResponse)
                    .build();
            });
    } // end createPropertyStatus method

    @PUT
    @Path("/{propertyStatusId}")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updatePropertyStatus(UUID propertyStatusId, @Valid ReqCreateUpdatePropertyStatusDto propertyStatusDto) {
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();

        // Call the service to update the property status
        return propertyStatusService.editPropertyStatus(propertyStatusId, propertyStatusDto, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Update Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }

                PropertyStatus updatedPropertyStatus = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Property status updated successfully", updatedPropertyStatus);
                return Response.ok(successResponse).build();
            });
    } // end updatePropertyStatus method


    @DELETE
    @Path("/{propertyStatusId}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> deletePropertyStatus(UUID propertyStatusId) {
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();

        // Call the service to delete the property status
        return propertyStatusService.removePropertyStatus(propertyStatusId, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Deletion Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }

                SuccessResponse successResponse = new SuccessResponse("Property status deleted successfully", null);
                return Response.ok(successResponse).build();
            });
    } // end deletePropertyStatus method


    @GET
    @Path("/{propertyStatusId}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> getPropertyStatusById(UUID propertyStatusId) {
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();

        // Call the service to get the property status by ID
        return propertyStatusService.viewPropertyStatusById(propertyStatusId, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Retrieval Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                if (result.getRight().isEmpty()) {
                    ErrorResponse errorResponse = new ErrorResponse("Not Found", "Property status not found with ID: " + propertyStatusId);
                    return Response.status(Response.Status.NOT_FOUND)
                        .entity(errorResponse)
                        .build();
                }
                // If the property status is found, return it in the response
                PropertyStatus propertyStatus = result.getRight().get();
                SuccessResponse successResponse = new SuccessResponse("Property status retrieved successfully", propertyStatus);
                return Response.ok(successResponse).build();
            });
    } // end getPropertyStatusById method


    @GET
    @Path("/")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> getUserPropertyStatus() {
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();

        // Call the service to get all property statuses for the user
        return propertyStatusService.viewAllUserPropertyStatus(userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Retrieval Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }

                SuccessResponse successResponse = new SuccessResponse("Property statuses retrieved successfully", result.getRight());
                return Response.ok(successResponse).build();
            });
    } // end getUserPropertyStatuses method

}// end class PropertyStatusController
