package com.otterdev.application.handler.auth;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;

import com.otterdev.application.usecase.internal.base.InternalUserUsecase;
import com.otterdev.domain.valueObject.dto.user.ReqLoginDto;
import com.otterdev.domain.valueObject.helper.response.ErrorResponse;
import com.otterdev.domain.valueObject.helper.response.SuccessResponse;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@SecuritySchemes(value = {
    @SecurityScheme(
        securitySchemeName = "jwt",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
    )
})
@ApplicationScoped
public class AuthHandler {
    
    private final InternalUserUsecase internalUserUsecase;

    @Inject
    public AuthHandler(InternalUserUsecase internalUserUsecase) {
        this.internalUserUsecase = internalUserUsecase;
    }



    // login
    @POST
    @Path("/login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> login(@Valid ReqLoginDto reqLoginDto) {
        // Implementation for login
        return internalUserUsecase.login(reqLoginDto)
                .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to login. Please check your credentials."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.CREATED)
                    .entity(new SuccessResponse("Login successfull", success))
                    .build()
            ));
    } // end class

    // get me
    

}
