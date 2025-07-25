package com.otterdev.infrastructure.service.implService.base;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

import com.otterdev.domain.entity.Gender;
import com.otterdev.domain.entity.Role;
import com.otterdev.domain.entity.User;
import com.otterdev.domain.valueObject.dto.token.ResUserTokenDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeEmailDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangePasswordDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeUserInfoDto;
import com.otterdev.domain.valueObject.dto.user.ReqChangeUsernameDto;
import com.otterdev.domain.valueObject.dto.user.ReqCreateUserDto;
import com.otterdev.domain.valueObject.dto.user.ReqLoginDto;
import com.otterdev.domain.valueObject.jwt.JwtClaimDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.GenderRepository;
import com.otterdev.infrastructure.repository.RoleRepository;
import com.otterdev.infrastructure.repository.UserRepository;
import com.otterdev.infrastructure.service.config.JwtService;
import com.otterdev.infrastructure.service.internal.base.InternalUserService;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("userService")  // Add this qualifier
class UserServiceImpl implements InternalUserService {

    private final UserRepository userRepository;
    private final GenderRepository genderRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService; // Assuming you have a JwtService for token generation

    @Inject
    public UserServiceImpl(UserRepository userRepository, GenderRepository genderRepository, RoleRepository roleRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.genderRepository = genderRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
    }
  
@Override
@WithTransaction
public Uni<Either<ServiceError, User>> register(ReqCreateUserDto userDto) {
    // Hash password synchronously before starting reactive chain
    String hashedPassword;
    try {
        hashedPassword = BCrypt.hashpw(userDto.getPassword().trim(), BCrypt.gensalt(12));
    } catch (Exception e) {
        return Uni.createFrom().item(
            Either.left(new ServiceError.OperationFailed("Password hashing failed"))
        );
    }

    // Create user with hashed password
    User newUser = new User();
    LocalDateTime now = LocalDateTime.now();
    newUser.setEmail(userDto.getEmail().trim());
    newUser.setPassword(hashedPassword);
    newUser.setUsername(userDto.getUsername().trim());
    newUser.setFirstName(userDto.getFirstName().trim());
    newUser.setLastName(userDto.getLastName().trim());
    newUser.setDob(userDto.getDob());
    newUser.setCreatedAt(now);
    newUser.setUpdatedAt(now);
    // Start reactive chain on event loop
    return genderRepository.findBydetail(userDto.getGender().toString())
        .chain(genderOpt -> {
            if (genderOpt.isEmpty()) {
                return Uni.createFrom().item(
                    Either.left(new ServiceError.NotFound("Gender not found"))
                );
            }
            newUser.setGender(genderOpt.get());
            return roleRepository.getUserRole();
        })
        .chain(roleOpt -> {
            if (roleOpt == null) {
                return Uni.createFrom().item(
                    Either.left(new ServiceError.NotFound("Role not found"))
                );
            }
            newUser.setRole((Role) roleOpt);
            return userRepository.persist(newUser)
                .map(persisted -> Either.<ServiceError, User>right(persisted));
        });
}

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, ResUserTokenDto>> login(ReqLoginDto loginDto) {
        return userRepository.findByEmail(loginDto.getEmail().trim())
            .map(userOpt -> {
                // Handle empty Optional
                if (userOpt.isEmpty()) {
                    ServiceError notFoundError = new ServiceError.NotFound("User not found with email: " + loginDto.getEmail());
                    return Either.<ServiceError, ResUserTokenDto>left(notFoundError);
                }

                User user = userOpt.get();
                
                // Verify password
                if (!BCrypt.checkpw(loginDto.getPassword().trim(), user.getPassword())) {
                    ServiceError invalidCredentialsError = new ServiceError.ValidationFailed("Invalid email or password");
                    return Either.<ServiceError, ResUserTokenDto>left(invalidCredentialsError);
                }

                try {
                    // Validate user data before creating claims
                    if (user.getId() == null) {
                        return Either.<ServiceError, ResUserTokenDto>left(
                            new ServiceError.OperationFailed("User ID is missing")
                        );
                    }
                    
                    if (user.getRole() == null || user.getRole().getDetail() == null) {
                        return Either.<ServiceError, ResUserTokenDto>left(
                            new ServiceError.OperationFailed("User role information is missing")
                        );
                    }

                    // Create JWT claims with validation
                    JwtClaimDto claims = new JwtClaimDto();
                    claims.setSubject(user.getId().toString());
                    claims.setGroups(Set.of(user.getRole().getDetail()));
                    claims.setExpiresInMillis(360000); // 1 hour in milliseconds
                    

                    // Generate JWT token
                    String token = jwtService.generateJwt(claims);
                    if (token == null || token.isEmpty()) {
                        return Either.<ServiceError, ResUserTokenDto>left(
                            new ServiceError.OperationFailed("Failed to generate valid token")
                        );
                    }

                    // Create response
                    ResUserTokenDto response = new ResUserTokenDto();
                    response.setToken(token);
                    

                    return Either.<ServiceError, ResUserTokenDto>right(response);
                } catch (Exception e) {
                    return Either.<ServiceError, ResUserTokenDto>left(
                        new ServiceError.OperationFailed("Token generation failed: " + e.getMessage())
                    );
                }
            });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> changePassword(ReqChangePasswordDto changePasswordDto, UUID userId) {
        
        return userRepository.findByUserId(userId)
                .chain(userOps -> {
                    if (userOps.isEmpty()) {
                        return Uni.createFrom().item(
                            Either.left(new ServiceError.NotFound("User not found with ID: " + userId))
                        );
                    }

                    User user = (User)userOps.get();
                    // Verify old password
                    if (!BCrypt.checkpw(changePasswordDto.getOldPassword().trim(), user.getPassword())) {
                        return Uni.createFrom().item(
                            Either.left(new ServiceError.ValidationFailed("Invalid old password"))
                        );
                    }

                    // Hash new password
                    String hashedNewPassword = BCrypt.hashpw(changePasswordDto.getNewPassword().trim(), BCrypt.gensalt(12));
                    user.setPassword(hashedNewPassword);
                    user.setUpdatedAt(LocalDateTime.now());

                    return userRepository.persist(user)
                        .map(persisted -> Either.<ServiceError, Boolean>right(true));
                });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> changeEmail(ReqChangeEmailDto changeEmailDto, UUID userId) {
        
        return userRepository.existsByEmail(changeEmailDto.getNewEmail().trim())
            .chain(exists -> {
                if (exists) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.ValidationFailed("Email already exists"))
                    );
                }
                return userRepository.findByUserId(userId)
                    .chain(userOps -> {
                        if (userOps.isEmpty()) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.NotFound("User not found with ID: " + userId))
                            );
                        }
                        User user = (User)userOps.get();
                        user.setEmail(changeEmailDto.getNewEmail().trim());
                        user.setUpdatedAt(LocalDateTime.now());
                        return userRepository.persist(user)
                            .map(persisted -> Either.<ServiceError, Boolean>right(true));
                    });
            });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> changeUsername(ReqChangeUsernameDto changeUsernameDto, UUID userId) {
        
        return userRepository.existsByUsername(changeUsernameDto.getNewUsername().trim())
            .chain(exists -> {
                if (exists) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.ValidationFailed("Username already exists"))
                    );
                }
                return userRepository.findByUserId(userId)
                    .chain(userOps -> {
                        if (userOps.isEmpty()) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.NotFound("User not found with ID: " + userId))
                            );
                        }
                        User user = (User)userOps.get();
                        user.setUsername(changeUsernameDto.getNewUsername().trim());
                        user.setUpdatedAt(LocalDateTime.now());
                        return userRepository.persist(user)
                            .map(persisted -> Either.<ServiceError, Boolean>right(true));
                    });
            });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> changeUserInfo(ReqChangeUserInfoDto changeUserInfoDto, UUID userId) {
        
        return genderRepository.findBydetail(changeUserInfoDto.getGender())
            .chain(genderOpt -> {
                if (genderOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Gender not found value : " + changeUserInfoDto.getGender()))
                    );
                }
                return userRepository.findByUserId(userId)
                    .chain(userOps -> {
                        if (userOps.isEmpty()) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.NotFound("User not found with ID: " + userId))
                            );
                        }
                        User user = (User)userOps.get();
                        Gender gender = (Gender)genderOpt.get();
                        user.setFirstName(changeUserInfoDto.getFirstName().trim());
                        user.setLastName(changeUserInfoDto.getLastName().trim());
                        user.setDob(changeUserInfoDto.getDob());
                        user.setGender(gender);
                        user.setUpdatedAt(LocalDateTime.now());
                        return userRepository.persist(user)
                            .map(persisted -> Either.<ServiceError, Boolean>right(true));
            });
            });
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, User>> getMe(UUID userId) {
        
        return userRepository.findByUserId(userId)
            .map(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Either.<ServiceError, User>left(new ServiceError.NotFound("User not found with ID: " + userId));
                }
                return Either.<ServiceError, User>right(userOpt.get());
            });
    }

    
    
}
