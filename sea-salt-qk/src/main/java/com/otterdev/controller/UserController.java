package com.otterdev.controller;

import com.otterdev.dto.entity.user.ReqChangePassword;
import com.otterdev.dto.entity.user.ReqLoginDto;
import com.otterdev.dto.entity.user.ReqRegisterDto;
import com.otterdev.dto.entity.user.TokenDto;
import com.otterdev.dto.entity.user.ReqChangeUserInfo;
import com.otterdev.dto.helper.response.ErrorResponse;
import com.otterdev.dto.helper.response.SuccessResponse;
import com.otterdev.entity.table.User;
import com.otterdev.error.service.ServiceError;
import com.otterdev.service.JwtService;
import com.otterdev.service.UserService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/users")
@ApplicationScoped
public class UserController {
    
    @Inject
    UserService userService;

    @Inject
    JwtService jwtService;

    @POST
    @Path("/register")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> registerUser(@Valid ReqRegisterDto userDetail) {
        // Call the UserService to handle registration logic
        return userService.register(userDetail)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Registration Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                User newUser = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("User created successfully", newUser);
                return Response.status(Response.Status.CREATED)
                    .entity(successResponse)
                    .build();
            });
    } // end registerUser method


    @POST
    @Path("/login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> login(@Valid ReqLoginDto loginDetail) {
        // Call the UserService to handle login logic
        return userService.login(loginDetail.getEmail(), loginDetail.getPassword())
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Login Failed", error.message());
                    return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(errorResponse)
                        .build();
                }
                String token = result.getRight();
                TokenDto tokenPayload = new TokenDto(token);
                SuccessResponse successResponse = new SuccessResponse("Login successful", tokenPayload);
                return Response.ok(successResponse).build();
            });
    }// end login method

    @POST
    @Path("/change-password")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> changePassword(@Valid ReqChangePassword changePasswordRequest) {
        // Extract user ID from JWT token using JwtService
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        
        UUID userId = userIdOpt.get();
        
        // Call the UserService to handle password change logic
        return userService.changePassword(userId, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword())
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Change Password Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                SuccessResponse successResponse = new SuccessResponse("Password changed successfully", null);
                return Response.ok(successResponse).build();
            });
    } // end changePassword method

    @POST
    @Path("/change-username")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> changeUsername(@Valid String newUsername) {
        // Extract user ID from JWT token using JwtService
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        
        UUID userId = userIdOpt.get();
        
        return userService.changeUsername(userId, newUsername)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Change Username Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                SuccessResponse successResponse = new SuccessResponse("Username changed successfully", null);
                return Response.ok(successResponse).build();
            });
    } // end changeUsername method

    @POST  
    @Path("/update-profile")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateUserInfo(@Valid ReqChangeUserInfo userInfoRequest) {
        // Extract user ID from JWT token using JwtService
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        
        UUID userId = userIdOpt.get();
        
        return userService.changeUserInfo(userInfoRequest, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Update Profile Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                User updatedUser = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Profile updated successfully", updatedUser);
                return Response.ok(successResponse).build();
            });
    } // end updateUserInfo method

    @GET
    @Path("/user-info")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getUserInfo() {
        // Extract user ID from JWT token
        Optional<UUID> currentUserIdOpt = jwtService.getCurrentUserId();
        if (currentUserIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID currentUserId = currentUserIdOpt.get();
        
        return userService.getUserInfo(currentUserId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("User Info Retrieval Failed", error.message());
                    return Response.status(Response.Status.NOT_FOUND)
                        .entity(errorResponse)
                        .build();
                }
                
                SuccessResponse successResponse = new SuccessResponse("User info retrieved successfully", result.getRight());
                return Response.ok(successResponse).build();
            });
    }

} // end class
