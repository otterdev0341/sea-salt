package com.otterdev.application.handler.user;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;

import com.otterdev.application.usecase.internal.base.InternalUserUsecase;
import com.otterdev.domain.valueObject.dto.user.ReqChangeEmailDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangePasswordDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeUserInfoDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeUsernameDto;
import com.otterdev.domain.valueObject.dto.user.ReqCreateUserDto;
import com.otterdev.domain.valueObject.helper.response.ErrorResponse;
import com.otterdev.domain.valueObject.helper.response.SuccessResponse;
import com.otterdev.infrastructure.service.config.JwtService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;




@Path("/user")
@SecuritySchemes(value = {
    @SecurityScheme(
        securitySchemeName = "jwt",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
    )
})
@ApplicationScoped
public class UserHandler {
    
    private final InternalUserUsecase internalUserUsecase;
    private final JwtService jwtService;
    
    @Inject
    public UserHandler(InternalUserUsecase internalUserUsecase, JwtService jwtService) {
        this.internalUserUsecase = internalUserUsecase;
        this.jwtService = jwtService;
    }



    // register
    @POST
    @Path("/register")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> register(@Valid ReqCreateUserDto reqRegisterDto) {
        // Implementation for user registration
        return internalUserUsecase.register(reqRegisterDto)
                .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to register user. Please check your details."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.CREATED)
                    .entity(new SuccessResponse("Registration successful", success))
                    .build()
            ));
    } // end register
    
    // change password
    @POST
    @Path("/change-password")
    @RolesAllowed("user")
    @SecurityRequirement(name = "jwt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> changePassword(@Valid ReqChangePasswordDto reqChangePasswordDto) {
        
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
        
        // Implementation for changing password
        return internalUserUsecase.changePassword(
            reqChangePasswordDto,
            userIdOpt.get()
        ).onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to change password. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Password changed successfully", success))
                    .build()
            ));
    } // end class

    // change username
    @POST
    @Path("/change-username")
    @RolesAllowed("user")
    @SecurityRequirement(name = "jwt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> changeUsername(@Valid ReqChangeUsernameDto reqChangeUsernameDto) {
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
        
        // Implementation for changing username
        return internalUserUsecase.changeUsername(
            reqChangeUsernameDto,
            userIdOpt.get()
        ).onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to change username. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Username changed successfully", success))
                    .build()
            ));
    } // end change username


    // change email

    @POST
    @Path("/change-email")
    @RolesAllowed("user")
    @SecurityRequirement(name = "jwt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> changeEmail(@Valid ReqChangeEmailDto reqChangeEmailDto) {
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
        
        // Implementation for changing email
        return internalUserUsecase.changeEmail(
            reqChangeEmailDto,
            userIdOpt.get()
        ).onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to change email. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Email changed successfully", success))
                    .build()
            ));
    } // end change email
    
    
    // change userinfo
    @POST
    @Path("/change-userinfo")
    @RolesAllowed("user")
    @SecurityRequirement(name = "jwt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> changeUserInfo(@Valid ReqChangeUserInfoDto reqChangeUserInfoDto) {
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
        
        // Implementation for changing user info
        return internalUserUsecase.changeUserInfo(
            reqChangeUserInfoDto,
            userIdOpt.get()
        ).onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to change user info. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("User info changed successfully", success))
                    .build()
            ));
    } // end change userinfo


    // get me
    @POST
    @Path("/me")
    @SecurityRequirement(name = "jwt")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON) // This is not necessary for GET requests, but included
    public Uni<Response> getMe() {
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
        
        // Implementation for getting user info
        return internalUserUsecase.getMe(userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve user information."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("User information retrieved successfully", success))
                    .build()
            ));
    } // end get me


}
