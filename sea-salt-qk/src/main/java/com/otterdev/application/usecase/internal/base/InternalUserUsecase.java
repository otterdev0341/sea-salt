package com.otterdev.application.usecase.internal.base;

import java.util.UUID;

import com.otterdev.domain.entity.User;
import com.otterdev.domain.valueObject.dto.token.ResUserTokenDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeEmailDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangePasswordDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeUserInfoDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeUsernameDto;
import com.otterdev.domain.valueObject.dto.user.ReqCreateUserDto;
import com.otterdev.domain.valueObject.dto.user.ReqLoginDto;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalUserUsecase {
    
    @WithTransaction
    Uni<Either<UsecaseError, User>> register(ReqCreateUserDto userDto);

    @WithSession
    Uni<Either<UsecaseError, ResUserTokenDto>> login(ReqLoginDto loginDto);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> changePassword(ReqChangePasswordDto changePasswordDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> changeEmail(ReqChangeEmailDto changeEmailDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> changeUsername(ReqChangeUsernameDto changeUsernameDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> changeUserInfo(ReqChangeUserInfoDto changeUserInfoDto, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, User>> getMe(UUID userId); 
}
