package com.otterdev.application.usecase.implUsecase.base;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }

    @Override
    public Uni<Either<UsecaseError, ResUserTokenDto>> login(ReqLoginDto loginDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public Uni<Either<UsecaseError, Void>> changePassword(ReqChangePasswordDto changePasswordDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public Uni<Either<UsecaseError, Void>> changeEmail(ReqChangeEmailDto changeEmailDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeEmail'");
    }

    @Override
    public Uni<Either<UsecaseError, Void>> changeUsername(ReqChangeUsernameDto changeUsernameDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeUsername'");
    }

    @Override
    public Uni<Either<UsecaseError, Void>> changeUserInfo(ReqChangeUserInfoDto changeUserInfoDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeUserInfo'");
    }

    @Override
    public Uni<Either<UsecaseError, User>> getMe(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMe'");
    }
    


} // end class
