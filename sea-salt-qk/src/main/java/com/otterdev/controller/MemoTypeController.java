package com.otterdev.controller;

import java.util.Optional;
import java.util.UUID;

import com.otterdev.dto.entity.memoType.ReqCreateUpdateMemoTypeDto;
import com.otterdev.dto.helper.response.ErrorResponse;
import com.otterdev.dto.helper.response.SuccessResponse;
import com.otterdev.entity.table.MemoType;
import com.otterdev.error.service.ServiceError;
import com.otterdev.service.MemoTypeService;
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

@Path("/memo-types")
@ApplicationScoped
public class MemoTypeController {
    
    @Inject
    private MemoTypeService memoTypeService;

    @Inject
    private JwtService jwtService;

    @POST
    @Path("/")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createMemoType(@Valid ReqCreateUpdateMemoTypeDto memoTypeDto) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        // Call the service to create a new memo type
        return memoTypeService.newMemoType(memoTypeDto, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Memo Type Creation Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                MemoType newMemoType = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Memo type created successfully", newMemoType);
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
    public Uni<Response> updateMemoType(@PathParam("id") UUID memoTypeId, @Valid ReqCreateUpdateMemoTypeDto memoTypeDto) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return memoTypeService.editMemoType(memoTypeId, memoTypeDto, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Memo Type Update Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                MemoType updatedMemoType = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Memo type updated successfully", updatedMemoType);
                return Response.ok(successResponse).build();
            });
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteMemoType(@PathParam("id") UUID memoTypeId) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return memoTypeService.deleteMemoType(memoTypeId, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Memo Type Deletion Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                SuccessResponse successResponse = new SuccessResponse("Memo type deleted successfully", null);
                return Response.ok(successResponse).build();
            });
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getMemoTypeById(@PathParam("id") UUID memoTypeId) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return memoTypeService.viewMemoTypeById(memoTypeId, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Memo Type Retrieval Failed", error.message());
                    return Response.status(Response.Status.NOT_FOUND)
                        .entity(errorResponse)
                        .build();
                }
                
                MemoType memoType = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Memo type retrieved successfully", memoType);
                return Response.ok(successResponse).build();
            });
    }

    @GET
    @Path("/")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getUserMemoTypes() {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return memoTypeService.viewAllUserMemoTypes(userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Memo Types Retrieval Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                SuccessResponse successResponse = new SuccessResponse("Memo types retrieved successfully", result.getRight());
                return Response.ok(successResponse).build();
            });
    }

} // end class
