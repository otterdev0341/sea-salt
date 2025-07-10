package com.otterdev.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.otterdev.dto.entity.memoType.ReqCreateUpdateMemoTypeDto;
import com.otterdev.entity.table.MemoType;
import com.otterdev.entity.table.User;
import com.otterdev.error.service.ServiceError;
import com.otterdev.error.service.ValidationError;
import com.otterdev.error.service.ResourceNotFoundError;
import com.otterdev.error.service.UnexpectedError;
import com.otterdev.repository.table.MemoTypeRepository;
import com.otterdev.repository.table.UserRepository;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MemoTypeService {
    
    @Inject
    private MemoTypeRepository memoTypeRepository;

    @Inject
    private UserRepository userRepository;

    // newMemoType
    /*
     *  verify if user id exists
     *  the new memotype must not exist in database
     *  create new memotype
     *  return the created memotype
     */
    @WithTransaction
    public Uni<Either<ServiceError, MemoType>> newMemoType(ReqCreateUpdateMemoTypeDto memoTypeDto, UUID userId) {
        // Validate input
        if (memoTypeDto == null || memoTypeDto.getDetail() == null || memoTypeDto.getDetail().trim().isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("Invalid memo type data: detail cannot be empty")));
        }

        // Verify user exists
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, MemoType>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }
                
                User user = userOpt.get();
                String trimmedDetail = memoTypeDto.getDetail().trim();
                
                // Check if memo type with same detail already exists for this user
                return memoTypeRepository.find("detail = ?1 and createdBy.id = ?2", trimmedDetail, userId).count()
                    .chain(count -> {
                        if (count > 0) {
                            return Uni.createFrom().item(Either.<ServiceError, MemoType>left(
                                new ValidationError("Memo type with detail '" + trimmedDetail + "' already exists")
                            ));
                        }
                        
                        LocalDateTime now = LocalDateTime.now();
                        
                        // Create new MemoType entity
                        MemoType newMemoType = MemoType.builder()
                            .detail(trimmedDetail)
                            .createdBy(user)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();
                        
                        // Save to repository
                        return memoTypeRepository.persist(newMemoType)
                            .map(savedMemoType -> Either.<ServiceError, MemoType>right(savedMemoType))
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, MemoType>left(
                                    new UnexpectedError("Failed to create memo type: " + throwable.getMessage(), throwable)
                                )
                            );
                    });
            });
    }

    // editMemoType
    /*
     *  verify if user id exists
     *  verify if memotype id exists
     *  verify if memotype id belongs to user
     *  the name that user wants to update must not currently exist in database
     *  update the memotype
     *  return the updated memotype
     */
    @WithTransaction
    public Uni<Either<ServiceError, MemoType>> editMemoType(UUID memoTypeId, ReqCreateUpdateMemoTypeDto memoTypeDto, UUID userId) {
        // Validate input
        if (memoTypeDto == null || memoTypeDto.getDetail() == null || memoTypeDto.getDetail().trim().isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("Invalid memo type data: detail cannot be empty")));
        }

        // Verify user exists
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, MemoType>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }
                
                // Verify memo type exists and belongs to user
                return memoTypeRepository.findByIdAndUserId(memoTypeId, userId)
                    .chain(memoTypeOpt -> {
                        if (memoTypeOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.<ServiceError, MemoType>left(
                                new ResourceNotFoundError("Memo type not found with ID: " + memoTypeId + " or you don't have permission to access it")
                            ));
                        }
                        
                        MemoType memoType = memoTypeOpt.get();
                        String trimmedDetail = memoTypeDto.getDetail().trim();
                        
                        // Check if another memo type with same detail already exists for this user (excluding current one)
                        return memoTypeRepository.find("detail = ?1 and createdBy.id = ?2 and id != ?3", trimmedDetail, userId, memoTypeId).count()
                            .chain(count -> {
                                if (count > 0) {
                                    return Uni.createFrom().item(Either.<ServiceError, MemoType>left(
                                        new ValidationError("Another memo type with detail '" + trimmedDetail + "' already exists")
                                    ));
                                }
                                
                                // Update fields
                                memoType.setDetail(trimmedDetail);
                                memoType.setUpdatedAt(LocalDateTime.now());
                                
                                return memoTypeRepository.persist(memoType)
                                    .map(updatedMemoType -> Either.<ServiceError, MemoType>right(updatedMemoType))
                                    .onFailure().recoverWithItem(throwable -> 
                                        Either.<ServiceError, MemoType>left(
                                            new UnexpectedError("Failed to update memo type: " + throwable.getMessage(), throwable)
                                        )
                                    );
                            });
                    });
            });
    }

    // deleteMemoType
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> deleteMemoType(UUID memoTypeId, UUID userId) {
        // Verify user exists
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }
                
                // Verify memo type exists and belongs to user
                return memoTypeRepository.findByIdAndUserId(memoTypeId, userId)
                    .chain(memoTypeOpt -> {
                        if (memoTypeOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                                new ResourceNotFoundError("Memo type not found with ID: " + memoTypeId + " or you don't have permission to access it")
                            ));
                        }
                        
                        MemoType memoType = memoTypeOpt.get();
                        
                        return memoTypeRepository.delete(memoType)
                            .map(deleted -> Either.<ServiceError, Boolean>right(true))
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, Boolean>left(
                                    new UnexpectedError("Failed to delete memo type: " + throwable.getMessage(), throwable)
                                )
                            );
                    });
            });
    }

    // viewMemoTypeById
    @WithSession
    public Uni<Either<ServiceError, MemoType>> viewMemoTypeById(UUID memoTypeId, UUID userId) {
        // Verify user exists
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, MemoType>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }
                
                // Verify memo type exists and belongs to user
                return memoTypeRepository.findByIdAndUserId(memoTypeId, userId)
                    .map(memoTypeOpt -> {
                        if (memoTypeOpt.isEmpty()) {
                            return Either.<ServiceError, MemoType>left(
                                new ResourceNotFoundError("Memo type not found with ID: " + memoTypeId + " or you don't have permission to access it")
                            );
                        }
                        
                        return Either.<ServiceError, MemoType>right(memoTypeOpt.get());
                    })
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, MemoType>left(
                            new UnexpectedError("Failed to retrieve memo type: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }

    // ViewAllUserMemoType
    @WithSession
    public Uni<Either<ServiceError, List<MemoType>>> viewAllUserMemoTypes(UUID userId) {
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, List<MemoType>>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }
                
                User user = userOpt.get();
                return memoTypeRepository.find("createdBy", user).list()
                    .map(memoTypes -> Either.<ServiceError, List<MemoType>>right(memoTypes))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, List<MemoType>>left(
                            new UnexpectedError("Failed to retrieve user memo types: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }
}
