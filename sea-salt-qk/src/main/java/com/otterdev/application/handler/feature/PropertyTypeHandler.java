package com.otterdev.application.handler.feature;

import java.util.Optional;
import java.util.UUID;

import com.otterdev.application.usecase.internal.base.InternalPropertyTypeUsecase;
import com.otterdev.domain.valueObject.dto.propertyType.ReqCreatePropertyTypeDto;
import com.otterdev.domain.valueObject.dto.propertyType.ReqUpdatePropertyTypeDto;
import com.otterdev.domain.valueObject.helper.response.ErrorResponse;
import com.otterdev.domain.valueObject.helper.response.SuccessResponse;
import com.otterdev.infrastructure.service.config.JwtService;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

@ApplicationScoped
@Path("/property-types")
public class PropertyTypeHandler {
    
    private final InternalPropertyTypeUsecase propertyTypeUsecase;
    private final JwtService jwtService;

    @Inject
    public PropertyTypeHandler(InternalPropertyTypeUsecase propertyTypeUsecase, JwtService jwtService) {
        this.propertyTypeUsecase = propertyTypeUsecase;
        this.jwtService = jwtService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createPropertyType(ReqCreatePropertyTypeDto dto) {
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

        return propertyTypeUsecase.createPropertyType(dto, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to create property type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.CREATED)
                    .entity(new SuccessResponse("Property type created successfully", success))
                    .build()
            ));
    }

    @PUT
    @Path("/{propertyTypeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updatePropertyType(
            @PathParam("propertyTypeId") UUID propertyTypeId,
            ReqUpdatePropertyTypeDto dto) {
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

        return propertyTypeUsecase.updatePropertyType(propertyTypeId, dto, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to update property type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Property type updated successfully", success))
                    .build()
            ));
    }

    @DELETE
    @Path("/{propertyTypeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deletePropertyType(@PathParam("propertyTypeId") UUID propertyTypeId) {
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

        return propertyTypeUsecase.deletePropertyType(propertyTypeId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to delete property type"
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
    @Path("/{propertyTypeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getPropertyTypeById(@PathParam("propertyTypeId") UUID propertyTypeId) {
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

        return propertyTypeUsecase.getPropertyTypeById(propertyTypeId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve property type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Property type retrieved successfully", success))
                    .build()
            ));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllPropertyTypes() {
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

        return propertyTypeUsecase.getAllPropertyType(userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve property types"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Property types retrieved successfully", success))
                    .build()
            ));
    }
}
