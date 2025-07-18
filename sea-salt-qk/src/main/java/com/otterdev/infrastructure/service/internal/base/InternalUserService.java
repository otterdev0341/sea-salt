package com.otterdev.infrastructure.service.internal.base;

import java.util.UUID;

import com.otterdev.domain.entity.User;
import com.otterdev.domain.valueObject.dto.token.ResUserTokenDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeEmailDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangePasswordDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeUserInfoDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeUsernameDto;
import com.otterdev.domain.valueObject.dto.user.ReqCreateUserDto;
import com.otterdev.domain.valueObject.dto.user.ReqLoginDto;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalUserService {
    
    @WithTransaction
    Uni<Either<ServiceError, User>> register(ReqCreateUserDto userDto);

    @WithSession
    Uni<Either<ServiceError, ResUserTokenDto>> login(ReqLoginDto loginDto);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> changePassword(ReqChangePasswordDto changePasswordDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> changeEmail(ReqChangeEmailDto changeEmailDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> changeUsername(ReqChangeUsernameDto changeUsernameDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> changeUserInfo(ReqChangeUserInfoDto changeUserInfoDto, UUID userId);

    @WithSession
    Uni<Either<ServiceError, User>> getMe(UUID userId); 
}
