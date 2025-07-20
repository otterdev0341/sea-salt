package com.otterdev.application.handler.feature;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;

import com.otterdev.application.usecase.internal.base.InternalPropertyUsecase;
import com.otterdev.domain.valueObject.dto.property.ReqCreatePropertyDto;
import com.otterdev.domain.valueObject.dto.property.ReqUpdatePropertyDto;
import com.otterdev.domain.valueObject.helper.response.ErrorResponse;
import com.otterdev.domain.valueObject.helper.response.SuccessResponse;
import com.otterdev.infrastructure.service.config.JwtService;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/properties")
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
public class PropertyHandler {
    

    private final InternalPropertyUsecase propertyUsecase;
    private final JwtService jwtService;

    @Inject
    public PropertyHandler(InternalPropertyUsecase propertyUsecase, JwtService jwtService) {
        this.propertyUsecase = propertyUsecase;
        this.jwtService = jwtService;
    }


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createProperty(@BeanParam @Valid ReqCreatePropertyDto reqCreatePropertyDto) {

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

        return propertyUsecase.createProperty(reqCreatePropertyDto, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to create property"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();

                },
                property -> Response
                    .status(Response.Status.CREATED)
                    .entity(new SuccessResponse("Property created successfully", userIdOpt))
                    .build()
            ));
    } // end createProperty


    @PUT
    @Path("/{propertyId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateProperty(UUID propertyId, @Valid ReqUpdatePropertyDto reqUpdatePropertyDto) {

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

        return propertyUsecase.updateProperty(reqUpdatePropertyDto, propertyId , userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to update property"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();

                },
                property -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Property updated successfully", userIdOpt))
                    .build()
            ));
    } // end updateProperty


    @DELETE
    @Path("/{propertyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteProperty(UUID propertyId) {

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

        return propertyUsecase.deleteProperty(propertyId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to delete property"
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
    } // end deleteProperty


    @GET
    @Path("/{propertyId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> getPropertyById(UUID propertyId) {
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

        return propertyUsecase.getPropertyById(propertyId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve property"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();

                },
                property -> Response
                    .status(Response.Status.OK)
                    .entity(property)
                    .build()
            ));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllProperties() {
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

        return propertyUsecase.getAllProperties(userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve properties"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();

                },
                properties -> Response
                    .status(Response.Status.OK)
                    .entity(properties)
                    .build()
            ));
    }



    // Additional methods for filtering properties by type, status, etc. can be added here
    
    @GET
    @Path("/type/{propertyTypeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getPropertiesByType(UUID propertyTypeId) {
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

        return propertyUsecase.getPropertiesByType(propertyTypeId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve properties by type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();

                },
                properties -> Response
                    .status(Response.Status.OK)
                    .entity(properties)
                    .build()
            ));
    }


    @GET
    @Path("/status/{statusId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getPropertiesByStatus(UUID statusId) {
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

        return propertyUsecase.getPropertiesByStatus(statusId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve properties by status"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();

                },
                properties -> Response
                    .status(Response.Status.OK)
                    .entity(properties)
                    .build()
            ));
    }


    @GET
    @Path("/sold/{isSold}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getPropertiesBySold(Boolean isSold) {
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

        return propertyUsecase.getPropertiesBySold(isSold, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve properties by sold status"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();

                },
                properties -> Response
                    .status(Response.Status.OK)
                    .entity(properties)
                    .build()
            ));
    }

    @GET
    @Path("/files/{targetId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllFilesRelatedById(UUID targetId) {
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

        return propertyUsecase.getAllFilesRelatedById(targetId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve files related by ID"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();

                },
                files -> Response
                    .status(Response.Status.OK)
                    .entity(files)
                    .build()
            ));
    }

    @GET
    @Path("/images/{targetId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllImagesRelatedById(UUID targetId) {
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

        return propertyUsecase.getAllImagesRelatedById(targetId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve images related by ID"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();

                },
                images -> Response
                    .status(Response.Status.OK)
                    .entity(images)
                    .build()
            ));
    }


    @GET
    @Path("/pdfs/{targetId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllPdfRelatedById(UUID targetId) {
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

        return propertyUsecase.getAllPdfRelatedById(targetId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve PDFs related by ID"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();

                },
                pdfs -> Response
                    .status(Response.Status.OK)
                    .entity(pdfs)
                    .build()
            ));
    }

    @GET
    @Path("/other-files/{targetId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllOtherFileRelatedById(UUID targetId) {
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

        return propertyUsecase.getAllOtherFileRelatedById(targetId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve other files related by ID"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();

                },
                files -> Response
                    .status(Response.Status.OK)
                    .entity(files)
                    .build()
            ));
    }


} // end class
