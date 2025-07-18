package com.otterdev.application.handler.feature;


import java.util.Optional;
import java.util.UUID;

import com.otterdev.application.usecase.internal.base.InternalContactTypeUsecase;
import com.otterdev.domain.valueObject.dto.contactType.ReqCreateContactTypeDto;
import com.otterdev.domain.valueObject.dto.contactType.ReqUpdateContactTypeDto;
import com.otterdev.domain.valueObject.helper.response.ErrorResponse;
import com.otterdev.domain.valueObject.helper.response.SuccessResponse;
import com.otterdev.infrastructure.service.config.JwtService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;



@ApplicationScoped
@Path("/contact-types")
public class ContactTypeHandler {
    
    private final InternalContactTypeUsecase internalContactTypeUsecase;

    private final JwtService jwtService;

    @Inject
    public ContactTypeHandler(InternalContactTypeUsecase internalContactTypeUsecase, JwtService jwtService) {
        this.internalContactTypeUsecase = internalContactTypeUsecase;
        this.jwtService = jwtService;
    }
    

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createContactType(@Valid ReqCreateContactTypeDto reqCreateContactTypeDto) {
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

        return internalContactTypeUsecase.createContactType(reqCreateContactTypeDto, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to create contact type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.CREATED)
                    .entity(new SuccessResponse("Contact type created successfully", success))
                    .build()
            ));
    }// end create



    @PUT
    @Path("/{contactTypeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateContactType(UUID contactTypeId, @Valid ReqUpdateContactTypeDto reqUpdateContactTypeDto) {
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

        return internalContactTypeUsecase.updateContactType(reqUpdateContactTypeDto,  contactTypeId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to update contact type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Contact type updated successfully", success))
                    .build()
            ));
    } // end update

    @DELETE
    @Path("/{contactTypeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteContactType(UUID contactTypeId) {
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

        return internalContactTypeUsecase.deleteContactType(contactTypeId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to delete contact type"
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
    } // end delete


    @GET
    @Path("/{contactTypeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getContactTypeById(UUID contactTypeId) {
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

        return internalContactTypeUsecase.getContactTypeById(contactTypeId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve contact type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Contact type retrieved successfully", success))
                    .build()
            ));
    } // end getContactTypeById


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllContactTypes() {
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

        return internalContactTypeUsecase.getAllContactTypes(userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve contact types"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Contact types retrieved successfully", success))
                    .build()
            ));
    } // end getAllContactTypes

} // end class
