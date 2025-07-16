package com.otterdev.infrastructure.service.implService.base;

import com.otterdev.domain.entity.User;
import com.otterdev.domain.valueObject.dto.token.ResUserTokenDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeEmailDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangePasswordDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeUserInfoDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeUsernameDto;
import com.otterdev.domain.valueObject.dto.user.ReqCreateUserDto;
import com.otterdev.domain.valueObject.dto.user.ReqLoginDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.GenderRepository;
import com.otterdev.infrastructure.repository.RoleRepository;
import com.otterdev.infrastructure.repository.UserRepository;
import com.otterdev.infrastructure.service.internal.base.InternalUserService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class UserServiceImpl implements InternalUserService {

    private final UserRepository userRepository;
    private final GenderRepository genderRepository;
    private final RoleRepository roleRepository;
  
    @Inject
    public UserServiceImpl(UserRepository userRepository, GenderRepository genderRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.genderRepository = genderRepository;
        this.roleRepository = roleRepository;
    }




    @Override
    public Uni<Either<ServiceError, User>> register(ReqCreateUserDto userDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }

    @Override
    public Uni<Either<ServiceError, ResUserTokenDto>> login(ReqLoginDto loginDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public Uni<Either<ServiceError, Void>> changePassword(ReqChangePasswordDto changePasswordDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public Uni<Either<ServiceError, Void>> changeEmail(ReqChangeEmailDto changeEmailDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeEmail'");
    }

    @Override
    public Uni<Either<ServiceError, Void>> changeUsername(ReqChangeUsernameDto changeUsernameDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeUsername'");
    }

    @Override
    public Uni<Either<ServiceError, Void>> changeUserInfo(ReqChangeUserInfoDto changeUserInfoDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeUserInfo'");
    }

    @Override
    public Uni<Either<ServiceError, User>> getMe(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMe'");
    }
    
}
