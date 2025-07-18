package com.otterdev.application.usecase.implUsecase.base;

import java.util.UUID;

import com.otterdev.application.usecase.internal.base.InternalUserUsecase;
import com.otterdev.domain.entity.User;
import com.otterdev.domain.valueObject.dto.token.ResUserTokenDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeEmailDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangePasswordDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeUserInfoDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeUsernameDto;
import com.otterdev.domain.valueObject.dto.user.ReqCreateUserDto;
import com.otterdev.domain.valueObject.dto.user.ReqLoginDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.base.InternalUserService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class UserUsecaseImpl implements InternalUserUsecase {

    private final InternalUserService userService;

    @Inject
    public UserUsecaseImpl(InternalUserService userService) {
        this.userService = userService;
    }

    @Override
    public Uni<Either<UsecaseError, User>> register(ReqCreateUserDto userDto) {
        // Call the user service to register a new user
        return userService.register(userDto)
            .onItem().transform(either -> either.fold(
                serviceError -> {
                    UsecaseError usecaseError = new UsecaseError.BusinessError(serviceError.message());
                    return Either.left(usecaseError);
                },
                success -> Either.right(success)
            ));

            
    }

    @Override
    public Uni<Either<UsecaseError, ResUserTokenDto>> login(ReqLoginDto loginDto) {
        // Call the user service to log in
        return userService.login(loginDto)
            .onItem().transform(either -> either.fold(
                serviceError -> {
                    UsecaseError usecaseError = new UsecaseError.Unauthorized(serviceError.message());
                    return Either.left(usecaseError);
                },
                success -> Either.right(success)
            ));
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> changePassword(ReqChangePasswordDto changePasswordDto, UUID userId) {
        // Call the user service to change the password
        return userService.changePassword(changePasswordDto, userId)
            .onItem().transform(either -> either.fold(
                serviceError -> {
                    UsecaseError usecaseError = new UsecaseError.InvalidRequest(serviceError.message());
                    return Either.left(usecaseError);
                },
                success -> Either.right(success) 
            ));
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> changeEmail(ReqChangeEmailDto changeEmailDto, UUID userId) {
        
        // Call the user service to change the email
        return userService.changeEmail(changeEmailDto, userId)
            .onItem().transform(either -> either.fold(
                serviceError -> {
                    UsecaseError usecaseError = new UsecaseError.InvalidRequest(serviceError.message());
                    return Either.left(usecaseError);
                },
                success -> Either.right(success) 
            ));
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> changeUsername(ReqChangeUsernameDto changeUsernameDto, UUID userId) {
        // Call the user service to change the username
        return userService.changeUsername(changeUsernameDto, userId)
            .onItem().transform(either -> either.fold(
                serviceError -> {
                    UsecaseError usecaseError = new UsecaseError.InvalidRequest(serviceError.message());
                    return Either.left(usecaseError);
                },
                success -> Either.right(success) 
            ));
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> changeUserInfo(ReqChangeUserInfoDto changeUserInfoDto, UUID userId) {
        // Call the user service to change user info
        return userService.changeUserInfo(changeUserInfoDto, userId)
            .onItem().transform(either -> either.fold(
                serviceError -> {
                    UsecaseError usecaseError = new UsecaseError.InvalidRequest(serviceError.message());
                    return Either.left(usecaseError);
                },
                success -> Either.right(success) 
            ));
    }

    @Override
    public Uni<Either<UsecaseError, User>> getMe(UUID userId) {
        // Call the user service to get the current user's information
        return userService.getMe(userId.toString())
            .onItem().transform(either -> either.fold(
                serviceError -> {
                    UsecaseError usecaseError = new UsecaseError.NotFound(serviceError.message());
                    return Either.left(usecaseError);
                },
                success -> Either.right(success)
            ));
    }


    
    


} // end class
