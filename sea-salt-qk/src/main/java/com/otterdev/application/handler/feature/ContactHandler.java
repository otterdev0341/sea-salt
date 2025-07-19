package com.otterdev.application.handler.feature;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;

import com.otterdev.application.usecase.internal.base.InternalContactUsecase;
import com.otterdev.domain.valueObject.dto.contact.ReqCreateContactDto;
import com.otterdev.domain.valueObject.dto.contact.ReqUpdateContactDto;
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

@Path("/contacts")
@SecuritySchemes(value = {
    @SecurityScheme(
        securitySchemeName = "jwt",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
    )
})
@SecurityRequirement(name = "jwt")
@ApplicationScoped
public class ContactHandler {
    

    private final InternalContactUsecase internalContactUsecase;
    private final JwtService jwtService;

    @Inject
    public ContactHandler(InternalContactUsecase internalContactUsecase, JwtService jwtService) {
        this.internalContactUsecase = internalContactUsecase;
        this.jwtService = jwtService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createContact(@Valid ReqCreateContactDto reqCreateContactDto) {
        
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

        return internalContactUsecase.createContact(reqCreateContactDto, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to create contact"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                contact -> Response
                    .status(Response.Status.CREATED)
                    .entity(new SuccessResponse("Contact created successfully", contact))
                    .build()
            ));
    } // end createContact


    @PUT
    @Path("/{contactId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateContact(UUID contactId, @Valid ReqUpdateContactDto reqUpdateContactDto) {
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

        return internalContactUsecase.updateContact(reqUpdateContactDto, contactId , userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to update contact"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                contact -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Contact updated successfully", contact))
                    .build()
            ));
    } // end updateContact


    @DELETE
    @Path("/{contactId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteContact(UUID contactId) {
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

        return internalContactUsecase.deleteContact(contactId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to delete contact"
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
    } // end deleteContact

    @GET
    @Path("/{contactId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getContactById(UUID contactId) {
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

        return internalContactUsecase.getContactById(contactId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve contact"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                contact -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Contact retrieved successfully", contact))
                    .build()
            ));
    } // end getContactById


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllContact() {
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

        return internalContactUsecase.getAllContacts(userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve contacts"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                contacts -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Contacts retrieved successfully", contacts))
                    .build()
            ));
    } // end getAllContacts


    @GET
    @Path("/contact-type/{contactTypeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllContactsByContactType(UUID contactTypeId) {
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

        return internalContactUsecase.getAllContactsByContactType(contactTypeId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve contacts by contact type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                contacts -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Contacts by contact type retrieved successfully", contacts))
                    .build()
            ));
    } // end getAllContactsByContactType

} // end class
