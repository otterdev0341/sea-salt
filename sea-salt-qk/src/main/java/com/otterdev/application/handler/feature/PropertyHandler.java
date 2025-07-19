package com.otterdev.application.handler.feature;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;

import com.otterdev.application.usecase.internal.base.InternalPropertyUsecase;
import com.otterdev.domain.valueObject.dto.property.ReqCreatePropertyDto;
import com.otterdev.domain.valueObject.helper.response.ErrorResponse;
import com.otterdev.domain.valueObject.helper.response.SuccessResponse;
import com.otterdev.infrastructure.service.config.JwtService;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
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

}
