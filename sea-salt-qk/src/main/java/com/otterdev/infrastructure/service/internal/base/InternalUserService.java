package com.otterdev.infrastructure.service.internal.base;

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
    Uni<Either<ServiceError, Void>> changePassword(ReqChangePasswordDto changePasswordDto);

    @WithTransaction
    Uni<Either<ServiceError, Void>> changeEmail(ReqChangeEmailDto changeEmailDto);

    @WithTransaction
    Uni<Either<ServiceError, Void>> changeUsername(ReqChangeUsernameDto changeUsernameDto);

    @WithTransaction
    Uni<Either<ServiceError, Void>> changeUserInfo(ReqChangeUserInfoDto changeUserInfoDto);

    @WithSession
    Uni<Either<ServiceError, User>> getMe(String userId); 
}
