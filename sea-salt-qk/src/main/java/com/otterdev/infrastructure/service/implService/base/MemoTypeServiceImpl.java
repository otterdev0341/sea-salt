package com.otterdev.infrastructure.service.implService.base;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.otterdev.domain.entity.MemoType;
import com.otterdev.domain.valueObject.dto.memoType.ReqCreateMemoTypeDto;
import com.otterdev.domain.valueObject.dto.memoType.ReqUpdateMemoTypeDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.MemoTypeRepository;
import com.otterdev.infrastructure.repository.UserRepository;
import com.otterdev.infrastructure.service.internal.base.InternalMemoTypeService;
import com.spencerwi.either.Either;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("memoTypeService")  // Add this qualifier
class MemoTypeServiceImpl implements InternalMemoTypeService {

    private final MemoTypeRepository memoTypeRepository;
    private final UserRepository userRepository; // Assuming you have a UserRepository for user-related operations

    @Inject
    public MemoTypeServiceImpl(MemoTypeRepository memoTypeRepository, UserRepository userRepository) {
        this.memoTypeRepository = memoTypeRepository;
        this.userRepository = userRepository;
    }
    

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, MemoType>> createMemoType(ReqCreateMemoTypeDto reqCreateMemoTypeDto, UUID userId) {

        MemoType newMemoType = new MemoType();
        LocalDateTime now = LocalDateTime.now();

        return memoTypeRepository.isExistByDetailAndUserId(reqCreateMemoTypeDto.getDetail().trim(), userId)
            .chain(isExist -> {
                if (isExist) {
                    ServiceError error = new ServiceError.DuplicateEntry("Memo type already exists");
                    return Uni.createFrom().item(Either.left(error));
                } 
                return userRepository.findByUserId(userId)
                    .chain(userOpt -> {
                        if (userOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                        }
                        newMemoType.setDetail(reqCreateMemoTypeDto.getDetail().trim());
                        newMemoType.setCreatedBy(userOpt.get());
                        newMemoType.setCreatedAt(now);
                        newMemoType.setUpdatedAt(now);
                        
                        return memoTypeRepository.persist(newMemoType)
                            .map(persistedMemoType -> Either.right(persistedMemoType));
                    });
            });
        
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, MemoType>> updateMemoType(UUID memoTypeId, 
            ReqUpdateMemoTypeDto reqUpdateMemoTypeDto, 
            UUID userId) {
        
        return memoTypeRepository.findByIdAndUserId(memoTypeId, userId)
            .chain(memoTypeOpt -> {
                if (memoTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Memo type not found"))
                    );
                }

                MemoType existingMemoType = memoTypeOpt.get();
                String newDetail = reqUpdateMemoTypeDto.getDetail().trim();

                // Check if the new detail is the same as current
                if (newDetail.equals(existingMemoType.getDetail())) {
                    return Uni.createFrom().item(
                        Either.right(existingMemoType)
                    );
                }

                // Check if detail already exists for another memo type
                return memoTypeRepository.isExistByDetailAndUserId(newDetail, userId)
                    .chain(exists -> {
                        if (exists) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.DuplicateEntry("Memo type with this detail already exists"))
                            );
                        }

                        // Update the memo type
                        existingMemoType.setDetail(newDetail);
                        existingMemoType.setUpdatedAt(LocalDateTime.now());

                        // Persist and fetch fresh copy with all relations
                        return memoTypeRepository.updateMemoType(existingMemoType, userId)
                            .chain(updatedResult -> {
                                if (updatedResult.isLeft()) {
                                    return Uni.createFrom().item(
                                        Either.left(new ServiceError.OperationFailed("Failed to update memo type: " + updatedResult.getLeft().message()))
                                    );
                                }
                                return memoTypeRepository.findByIdAndUserId(existingMemoType.getId(), userId)
                                    .map(memoType -> Either.right(memoType.orElse(null)));
                            });
                    });
            });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> deleteMemoType(UUID memoTypeId, UUID userId) {
        
        return memoTypeRepository.findByIdAndUserId(memoTypeId, userId)
            .chain(memoTypeOpt -> {
                if (memoTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Memo type not found"))
                    );
                }

                return memoTypeRepository.deleteMemoType(memoTypeOpt.get().getId(), userId)
                        .chain(result -> {
                            if (result.isLeft()) {
                                return Uni.createFrom().item(
                                    Either.left(new ServiceError.OperationFailed("Failed to delete memo type with id: " + memoTypeId))
                                );
                            }
                            return Uni.createFrom().item(Either.right(true));
                        }); 
            });
    } // end delete Memo Type

    @Override
    @WithSession
    public Uni<Either<ServiceError, MemoType>> getMemoType(UUID memoTypeId, UUID userId) {
        
        return memoTypeRepository.findByIdAndUserId(memoTypeId, userId)
            .chain(memoTypeOpt -> {
                if (memoTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Memo type not found"))
                    );
                }
                return Uni.createFrom().item(Either.right(memoTypeOpt.get()));
            });
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<MemoType>>> getAllMemoTypes(UUID userId) {
        
        return memoTypeRepository.findAllByUserId(userId)
            .chain(result -> {
                if (result.isLeft()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.OperationFailed("Failed to retrieve memo types: " + result.getLeft().message()))
                    );
                } else {
                    return Uni.createFrom().item(Either.right(result.getRight()));
                }
            });
    }
    
}
