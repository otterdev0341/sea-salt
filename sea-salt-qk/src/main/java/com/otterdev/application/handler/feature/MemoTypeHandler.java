package com.otterdev.application.handler.feature;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;

import com.otterdev.application.usecase.internal.base.InternalMemoTypeUsecase;
import com.otterdev.domain.valueObject.dto.memoType.ReqCreateMemoTypeDto;
import com.otterdev.domain.valueObject.dto.memoType.ReqUpdateMemoTypeDto;
import com.otterdev.domain.valueObject.helper.response.ErrorResponse;
import com.otterdev.domain.valueObject.helper.response.SuccessResponse;
import com.otterdev.infrastructure.service.config.JwtService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/memo-types")
@SecuritySchemes(value = {
    @SecurityScheme(
        securitySchemeName = "jwt",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
    )
})
@SecurityRequirement(name = "jwt")
public class MemoTypeHandler {
    
    private final InternalMemoTypeUsecase internalMemoTypeUsecase;
    private final JwtService jwtService;

    @Inject
    public MemoTypeHandler(InternalMemoTypeUsecase internalMemoTypeUsecase, JwtService jwtService) {
        this.internalMemoTypeUsecase = internalMemoTypeUsecase;
        this.jwtService = jwtService;
    }

    @POST
    @RolesAllowed({"user"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createMemoType(@Valid ReqCreateMemoTypeDto reqCreateMemoTypeDto) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        Optional<Set<String>> userGroups = jwtService.getCurrentUserGroups();
        // Add logging to debug JWT issues
        System.out.println("User ID from JWT: " + userIdOpt.orElse(null));
        System.out.println("User Groups from JWT: " + userGroups.orElse(null));

        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized", 
                "User not authenticated",
                Response.Status.UNAUTHORIZED
            );
            return Uni.createFrom().item(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }

        return internalMemoTypeUsecase.createMemoType(reqCreateMemoTypeDto, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to create memo type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.CREATED)
                    .entity(new SuccessResponse("Memo type created successfully", success))
                    .build()
            ));
    }

    @POST
    @Path("/{memoTypeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateMemoType(UUID memoTypeId, @Valid ReqUpdateMemoTypeDto reqUpdateMemoTypeDto) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized", 
                "User not authenticated",
                Response.Status.UNAUTHORIZED
            );
            return Uni.createFrom().item(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }

        return internalMemoTypeUsecase.updateMemoType(memoTypeId, reqUpdateMemoTypeDto, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to update memo type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Memo type updated successfully", success))
                    .build()
            ));
    }

    @DELETE
    @Path("/{memoTypeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteMemoType(UUID memoTypeId) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized", 
                "User not authenticated",
                Response.Status.UNAUTHORIZED
            );
            return Uni.createFrom().item(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }

        return internalMemoTypeUsecase.deleteMemoType(memoTypeId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to delete memo type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.NO_CONTENT)
                    .build()
            ));
    }

    @GET
    @Path("/{memoTypeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getMemoTypeById(UUID memoTypeId) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized", 
                "User not authenticated",
                Response.Status.UNAUTHORIZED
            );
            return Uni.createFrom().item(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }

        return internalMemoTypeUsecase.getMemoTypeById(memoTypeId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve memo type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Memo type retrieved successfully", success))
                    .build()
            ));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllMemoTypes() {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized", 
                "User not authenticated",
                Response.Status.UNAUTHORIZED
            );
            return Uni.createFrom().item(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }

        return internalMemoTypeUsecase.getAllMemoTypes(userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve memo types"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Memo types retrieved successfully", success))
                    .build()
            ));
    }
}
