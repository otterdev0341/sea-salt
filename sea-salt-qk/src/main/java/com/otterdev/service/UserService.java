package com.otterdev.service;



import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import com.otterdev.dto.entity.user.ReqChangeUserInfo;
import com.otterdev.dto.entity.user.ReqRegisterDto;
import com.otterdev.dto.entity.user.ResUserDetailDto;
import com.otterdev.dto.jwt.JwtClaimDto;
import com.otterdev.entity.table.Gender;
import com.otterdev.entity.table.User;
import com.otterdev.error.service.ServiceError;
import com.otterdev.error.service.UnexpectedError;
import com.otterdev.repository.table.GenderRepository;
import com.otterdev.repository.table.UserRepository;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {
    
    @Inject
    private UserRepository userRepository;

    @Inject
    private JwtService jwtService;

    @Inject
    private GenderRepository genderRepository;

    @WithTransaction
    public Uni<Either<ServiceError, User>> register(ReqRegisterDto userDetail) {
        return userRepository.existsByEmail(userDetail.getEmail())
            .flatMap(emailExists -> {
                if (emailExists) {
                    return Uni.createFrom().item(Either.<ServiceError, User>left(new UnexpectedError("Email already exists")));
                }
                
                return userRepository.existsByUsername(userDetail.getUsername())
                    .flatMap(usernameExists -> {
                        if (usernameExists) {
                            return Uni.createFrom().item(Either.<ServiceError, User>left(new UnexpectedError("Username already exists")));
                        }
                        
                        // hash password using actual user input
                        String hashedPassword = BCrypt.hashpw(userDetail.getPassword(), BCrypt.gensalt());

                        return genderRepository.findByName(userDetail.getGender().trim().toLowerCase())
                            .flatMap(gender -> {
                                if (gender == null) {
                                    return Uni.createFrom().item(Either.<ServiceError, User>left(new UnexpectedError("Gender not found")));
                                }

                                // create user entity
                                User newUser = new User();
                                newUser.setEmail(userDetail.getEmail());
                                newUser.setUsername(userDetail.getUsername());
                                newUser.setPassword(hashedPassword);
                                newUser.setFirstName(userDetail.getFirstName());
                                newUser.setLastName(userDetail.getLastName());
                                newUser.setGender(gender.getId());
                                newUser.setDob(userDetail.getDob());
                                newUser.setRole("user"); // default role is user
                                newUser.setCreatedAt(LocalDateTime.now());
                                newUser.setUpdatedAt(LocalDateTime.now());

                                // persist user entity
                                return userRepository.persist(newUser)
                                    .map(savedUser -> Either.<ServiceError, User>right(savedUser))
                                    .onFailure().recoverWithItem(throwable -> 
                                        Either.<ServiceError, User>left(new UnexpectedError("Failed to save user", throwable)));
                            });
                    });
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, User>left(new UnexpectedError("Failed to register user", throwable)));
    } // end register

    @WithSession
    public Uni<Either<ServiceError, String>> login(String email, String password) {
        return userRepository.findByEmail(email)
            .flatMap(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, String>left(new UnexpectedError("User not found")));
                }

                User user = userOpt.get();

                // check password
                if (!BCrypt.checkpw(password, user.getPassword())) {
                    return Uni.createFrom().item(Either.<ServiceError, String>left(new UnexpectedError("Invalid password")));
                }
                
                JwtClaimDto payload = new JwtClaimDto();
                payload.setSubject(user.getId().toString());
                payload.setGroups(Set.of(user.getRole()));
                payload.setExpiresInMillis(new Date().getTime() + 3600000); // 1 hour expiration
                
                String token = jwtService.generateJwt(payload);

                return Uni.createFrom().item(Either.<ServiceError, String>right(token));
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, String>left(new UnexpectedError("Failed to login", throwable)));
    }// end login 

    @WithTransaction
    public Uni<Either<ServiceError, User>> changePassword(UUID userId, String oldPassword, String newPassword) {
        return userRepository.findById(userId)
            .flatMap(optionalUser -> {
                if (optionalUser.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, User>left(new UnexpectedError("User not found")));
                }

                User user = optionalUser.get();

                // check old password
                if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
                    return Uni.createFrom().item(Either.<ServiceError, User>left(new UnexpectedError("Invalid old password")));
                }

                // hash new password
                String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                user.setPassword(hashedNewPassword);
                user.setUpdatedAt(LocalDateTime.now());

                // update user entity
                return userRepository.persist(user)
                    .map(updatedUser -> Either.<ServiceError, User>right(updatedUser))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, User>left(new UnexpectedError("Failed to change password", throwable)));
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, User>left(new UnexpectedError("Failed to change password", throwable)));
            
    } // end changePassword

    @WithTransaction
    public Uni<Either<ServiceError, User>> changeUsername(UUID userId, String newUsername) {
        return userRepository.existsByUsername(newUsername)
            .flatMap(usernameExists -> {
                if (usernameExists) {
                    return Uni.createFrom().item(Either.<ServiceError, User>left(new UnexpectedError("Username already exists")));
                }

                return userRepository.findById(userId)
                    .flatMap(optionalUser -> {
                        if (optionalUser.isEmpty()) {
                            return Uni.createFrom().item(Either.<ServiceError, User>left(new UnexpectedError("User not found")));
                        }

                        User user = optionalUser.get();
                        user.setUsername(newUsername);
                        user.setUpdatedAt(LocalDateTime.now());

                        return userRepository.persist(user)
                            .map(updatedUser -> Either.<ServiceError, User>right(updatedUser))
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, User>left(new UnexpectedError("Failed to change username", throwable)));
                    });
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, User>left(new UnexpectedError("Failed to change username", throwable)));    } // end changeUsername

    @WithTransaction
    public Uni<Either<ServiceError, User>> changeUserInfo(ReqChangeUserInfo userInfo, UUID userId) {
        return userRepository.findById(userId)
            .flatMap(optionalUser -> {
                if (optionalUser.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, User>left(new UnexpectedError("User not found")));
                }

                User user = optionalUser.get();

                // Verify password if provided
                if (userInfo.getPassword() != null && !userInfo.getPassword().trim().isEmpty()) {
                    if (!BCrypt.checkpw(userInfo.getPassword(), user.getPassword())) {
                        return Uni.createFrom().item(Either.<ServiceError, User>left(new UnexpectedError("Invalid password")));
                    }
                }
                
                // Update user fields
                if (userInfo.getFirstName() != null && !userInfo.getFirstName().trim().isEmpty()) {
                    user.setFirstName(userInfo.getFirstName());
                }
                
                if (userInfo.getLastName() != null && !userInfo.getLastName().trim().isEmpty()) {
                    user.setLastName(userInfo.getLastName());
                }
                
                if (userInfo.getDob() != null) {
                    user.setDob(userInfo.getDob());
                }

                // Handle gender update if provided
                if (userInfo.getGender() != null && !userInfo.getGender().trim().isEmpty()) {
                    return genderRepository.findByName(userInfo.getGender().trim().toLowerCase())
                        .flatMap(gender -> {
                            if (gender == null) {
                                return Uni.createFrom().item(Either.<ServiceError, User>left(new UnexpectedError("Gender not found")));
                            }
                            
                            user.setGender(gender.getId());
                            user.setUpdatedAt(LocalDateTime.now());
                            
                            return userRepository.persist(user)
                                .map(updatedUser -> Either.<ServiceError, User>right(updatedUser))
                                .onFailure().recoverWithItem(throwable -> 
                                    Either.<ServiceError, User>left(new UnexpectedError("Failed to update user info", throwable)));
                        });
                } else {
                    // Update user without gender change
                    user.setUpdatedAt(LocalDateTime.now());
                    return userRepository.persist(user)
                        .map(updatedUser -> Either.<ServiceError, User>right(updatedUser))
                        .onFailure().recoverWithItem(throwable -> 
                            Either.<ServiceError, User>left(new UnexpectedError("Failed to update user info", throwable)));
                }
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, User>left(new UnexpectedError("Failed to update user info", throwable)));
    } // end changeUserInfo

    @WithSession
    public Uni<Either<ServiceError, ResUserDetailDto>> getUserInfo(UUID userId) {
        return userRepository.findById(userId)
            .flatMap(optionalUser -> {
                if (optionalUser.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, ResUserDetailDto>left(new UnexpectedError("User not found")));
                }

                User user = optionalUser.get();
                
                // Find gender reactively
                return genderRepository.findById(user.getGender())
                    .flatMap(optionalGender -> {
                        if (optionalGender.isEmpty()) {
                            return Uni.createFrom().item(Either.<ServiceError, ResUserDetailDto>left(new UnexpectedError("Gender not found")));
                        }
                        
                        Gender gender = optionalGender.get();
                        
                        // Create response DTO
                        ResUserDetailDto userDetail = new ResUserDetailDto();
                        userDetail.setId(user.getId());
                        userDetail.setEmail(user.getEmail());
                        userDetail.setUsername(user.getUsername());
                        userDetail.setFirstName(user.getFirstName());
                        userDetail.setLastName(user.getLastName());
                        userDetail.setGender(gender.getName());
                        userDetail.setDob(user.getDob());
                        userDetail.setCreatedAt(user.getCreatedAt());
                        userDetail.setUpdatedAt(user.getUpdatedAt());

                        return Uni.createFrom().item(Either.<ServiceError, ResUserDetailDto>right(userDetail));
                    });
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, ResUserDetailDto>left(new UnexpectedError("Failed to get user info", throwable)));
    } // end getUserInfo

}// end class
