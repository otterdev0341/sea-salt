package com.otterdev.sea_salt.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.otterdev.sea_salt.config.jwt.JwtUtil;
import com.otterdev.sea_salt.dto.user.ReqChangeEmail;
import com.otterdev.sea_salt.dto.user.ReqChangePassword;
import com.otterdev.sea_salt.dto.user.ReqChangeUserInfo;
import com.otterdev.sea_salt.dto.user.ReqRegisterDto;
import com.otterdev.sea_salt.dto.user.TokenDto;
import com.otterdev.sea_salt.entity.User;
import com.otterdev.sea_salt.repository.GenderRepository;
import com.otterdev.sea_salt.repository.UserRepository;
import com.otterdev.sea_salt.soc.serviceError.EmailAlreadyExistsException;
import com.otterdev.sea_salt.soc.serviceError.InvalidGenderException;
import com.otterdev.sea_salt.soc.serviceError.LoginFailException;
import com.otterdev.sea_salt.soc.serviceError.UserNotFoundException;
import com.otterdev.sea_salt.soc.serviceError.UsernameAlreadyExistException;
import com.otterdev.sea_salt.utility.Result;

import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Mono<Result<User, RuntimeException>> register(ReqRegisterDto user) {
        return userRepository.findByEmail(user.getEmail())
                .hasElement()
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return Mono.just(Result.err(new EmailAlreadyExistsException("Email already exists")));
                    }
                    
                    // Check if username is unique
                    return userRepository.findByUsername(user.getUsername())
                            .hasElement()
                            .flatMap(usernameExists -> {
                                if (usernameExists) {
                                    return Mono.just(Result.err(new UsernameAlreadyExistException("Username already exists")));
                                }
                                
                                return genderRepository.findByName(user.getGender())
                                        .switchIfEmpty(Mono.error(new InvalidGenderException("Invalid gender: " + user.getGender())))
                                        .flatMap(gender -> {
                                            User newUser = new User();
                                            // newUser.setId(UUID.randomUUID().toString()); // Generate a new UUID
                                            newUser.setFirstName(user.getFirstName());
                                            newUser.setLastName(user.getLastName());
                                            newUser.setEmail(user.getEmail());
                                            newUser.setUsername(user.getUsername());
                                            newUser.setDob(user.getDob());

                                            // 🔐 Hash password
                                            String hashedPassword = passwordEncoder.encode(user.getPassword());
                                            newUser.setPassword(hashedPassword);

                                            // 👤 Set gender ID
                                            newUser.setGender(gender.getId());
                                            newUser.setRole("USER"); // Default role
                                            
                                            return userRepository.save(newUser)
                                                    .map(savedUser -> Result.<User, RuntimeException>ok(savedUser))
                                                    .onErrorResume(e -> Mono.just(Result.<User, RuntimeException>err(
                                                        new RuntimeException("Failed to save user: " + e.getMessage())
                                                    )));
                                        })
                                        .onErrorResume(e -> Mono.just(Result.<User, RuntimeException>err(
                                            e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e.getMessage())
                                        )));
                            });
                });
    }

    public Mono<Result<TokenDto, RuntimeException>> login(String email, String password) {
        return userRepository.findByEmail(email)
            .switchIfEmpty(Mono.error(new UserNotFoundException("Invalid email or password")))
            .flatMap(user -> {
                if (!passwordEncoder.matches(password, user.getPassword())) {
                    return Mono.just(Result.<TokenDto, RuntimeException>err(new LoginFailException("Invalid email or password")));
                }
                String token = jwtUtil.generateToken(user.getUsername());
                TokenDto tokenDto = new TokenDto();
                tokenDto.setToken(token);
                return Mono.just(Result.<TokenDto, RuntimeException>ok(tokenDto));
            })
            .onErrorResume(e -> Mono.just(Result.<TokenDto, RuntimeException>err(new LoginFailException("Invalid email or password"))));
    }



    public Mono<Result<User, RuntimeException>> changePassword(ReqChangePassword dto, UUID userId) {
    // Input validation
    if (dto == null || dto.getOldPassword() == null || dto.getNewPassword() == null) {
        return Mono.just(Result.err(new IllegalArgumentException("Old password and new password are required")));
    }
    
    // Optional: Add password strength validation
    if (dto.getNewPassword().length() < 8) {
        return Mono.just(Result.err(new IllegalArgumentException("New password must be at least 8 characters long")));
    }
    
    return userRepository.findById(userId)
        .cast(User.class) // Ensure proper type casting
        .flatMap(user -> {
            // Check if old password matches
            if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                return Mono.just(Result.<User, RuntimeException>err(
                    new LoginFailException("Old password is incorrect")
                ));
            }
            
            // Set new password and save
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            return userRepository.save(user)
                .map(savedUser -> Result.<User, RuntimeException>ok(savedUser))
                .onErrorResume(e -> {
                    // Log the actual error for debugging
                    
                    return Mono.just(Result.<User, RuntimeException>err(
                        new RuntimeException("Failed to change password: " + e.getMessage())
                    ));
                });
        })
        .switchIfEmpty(Mono.just(Result.<User, RuntimeException>err(
            new UserNotFoundException("User not found with id: " + userId)
        )))
        .onErrorResume(e -> {
            // Handle any unexpected errors
            
            return Mono.just(Result.<User, RuntimeException>err(
                new RuntimeException("An unexpected error occurred")
            ));
        });
}

    
    public Mono<Result<User, RuntimeException>> changeEmail(ReqChangeEmail emailDto, UUID userId) {
        // Input validation
        if (emailDto == null || emailDto.getNewEmail() == null || emailDto.getNewEmail().trim().isEmpty()) {
            return Mono.just(Result.err(new IllegalArgumentException("New email is required")));
        }
        
        // Email format validation
        if (!isValidEmail(emailDto.getNewEmail())) {
            return Mono.just(Result.err(new IllegalArgumentException("Invalid email format")));
        }
        
        // Password validation if required
        if (emailDto.getPassword() == null || emailDto.getPassword().trim().isEmpty()) {
            return Mono.just(Result.err(new IllegalArgumentException("Password is required to change email")));
        }
        
        return userRepository.findById(userId)
            .cast(User.class)
            .flatMap(user -> {
                // Verify password before allowing email change
                if (!passwordEncoder.matches(emailDto.getPassword(), user.getPassword())) {
                    return Mono.just(Result.<User, RuntimeException>err(
                        new LoginFailException("Incorrect password")
                    ));
                }
                
                // Check if new email is the same as current email
                if (user.getEmail().equals(emailDto.getNewEmail())) {
                    return Mono.just(Result.<User, RuntimeException>err(
                        new IllegalArgumentException("New email cannot be the same as current email")
                    ));
                }
                
                // Check if new email already exists
                return userRepository.findByEmail(emailDto.getNewEmail())
                    .hasElement()
                    .flatMap(emailExists -> {
                        if (emailExists) {
                            return Mono.just(Result.<User, RuntimeException>err(
                                new EmailAlreadyExistsException("Email already exists: " + emailDto.getNewEmail())
                            ));
                        }
                        
                        // Update email and save
                        user.setEmail(emailDto.getNewEmail());
                        // Optional: Set email verification flag if you have email verification
                        // user.setEmailVerified(false);
                        
                        return userRepository.save(user)
                            .map(savedUser -> Result.<User, RuntimeException>ok(savedUser))
                            .onErrorResume(e -> {
                                
                                return Mono.just(Result.<User, RuntimeException>err(
                                    new RuntimeException("Failed to change email: " + e.getMessage())
                                ));
                            });
                    });
            })
            .switchIfEmpty(Mono.just(Result.<User, RuntimeException>err(
                new UserNotFoundException("User not found with id: " + userId)
            )))
            .onErrorResume(e -> {
                
                return Mono.just(Result.<User, RuntimeException>err(
                    new RuntimeException("An unexpected error occurred")
                ));
            });
    }
    

    

    // Helper method for email validation
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }


    public Mono<Result<User, RuntimeException>> changeUserInfo(ReqChangeUserInfo dto, UUID userId) {
    // Input validation
    if (dto == null) {
        return Mono.just(Result.err(new IllegalArgumentException("User info is required")));
    }

    // Optional: Require password for user info change
    if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
        return Mono.just(Result.err(new IllegalArgumentException("Password is required to change user info")));
    }

    return userRepository.findById(userId)
        .cast(User.class)
        .flatMap(user -> {
            // Verify password before allowing info change
            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                return Mono.just(Result.<User, RuntimeException>err(
                    new LoginFailException("Incorrect password")
                ));
            }

            // Update fields if provided
            if (dto.getFirstName() != null && !dto.getFirstName().trim().isEmpty()) {
                user.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null && !dto.getLastName().trim().isEmpty()) {
                user.setLastName(dto.getLastName());
            }
            if (dto.getDob() != null) {
                user.setDob(dto.getDob());
            }

            // Handle gender update if provided
            if (dto.getGender() != null && !dto.getGender().trim().isEmpty()) {
                return genderRepository.findByName(dto.getGender())
                    .switchIfEmpty(Mono.error(new InvalidGenderException("Invalid gender: " + dto.getGender())))
                    .flatMap(gender -> {
                        user.setGender(gender.getId());
                        return userRepository.save(user)
                            .map(savedUser -> Result.<User, RuntimeException>ok(savedUser))
                            .onErrorResume(e -> Mono.just(Result.<User, RuntimeException>err(
                                new RuntimeException("Failed to update user info: " + e.getMessage())
                            )));
                    })
                    .onErrorResume(e -> Mono.just(Result.<User, RuntimeException>err(
                        e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e)
                    )));
            } else {
                // No gender update, just save
                return userRepository.save(user)
                    .map(savedUser -> Result.<User, RuntimeException>ok(savedUser))
                    .onErrorResume(e -> Mono.just(Result.<User, RuntimeException>err(
                        new RuntimeException("Failed to update user info: " + e.getMessage())
                    )));
            }
        })
        .switchIfEmpty(Mono.just(Result.<User, RuntimeException>err(
            new UserNotFoundException("User not found with id: " + userId)
        )))
        .onErrorResume(e -> Mono.just(Result.<User, RuntimeException>err(
            new RuntimeException("An unexpected error occurred")
        )));
}
}// end class