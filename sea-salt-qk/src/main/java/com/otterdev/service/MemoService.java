package com.otterdev.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.otterdev.dto.entity.memo.ReqCreateUpdateMemoDto;
import com.otterdev.entity.table.Memo;
import com.otterdev.entity.table.MemoType;
import com.otterdev.entity.table.User;
import com.otterdev.error.service.ResourceNotFoundError;
import com.otterdev.error.service.ServiceError;
import com.otterdev.error.service.ValidationError;
import com.otterdev.error.service.UnexpectedError;
import com.otterdev.repository.table.MemoRepository;
import com.otterdev.repository.table.MemoTypeRepository;
import com.otterdev.repository.table.UserRepository;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MemoService {
    
    @Inject
    private MemoRepository memoRepository;

    @Inject
    private MemoTypeRepository memoTypeRepository;

    @Inject
    private UserRepository userRepository;

    // newMemo
    /*
     * TODO: Implement the method to create a new memo.
     * check is user exist by using UserRepository,
     * It should validate the request, check if the memo type exists,by using MemoTypeRepository,
     * and then save the memo using MemoRepository.
     */
    @WithTransaction
    public Uni<Either<ServiceError, Memo>> newMemo(ReqCreateUpdateMemoDto memoDto, UUID userId) {
        // Validate input
        if (memoDto == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Memo data cannot be null")));
        }
        
        if (memoDto.getMemoType() == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Memo type ID is required")));
        }
        
        // First, fetch the User entity to ensure it exists
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Memo>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }

                User user = userOpt.get();
                
                // Then, fetch the MemoType entity to ensure it exists and user has access
                return memoTypeRepository.findByIdAndUserId(memoDto.getMemoType(), userId)
                    .chain(memoTypeOpt -> {
                        if (memoTypeOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.<ServiceError, Memo>left(
                                new ResourceNotFoundError("Memo type not found with ID: " + memoDto.getMemoType() + " or you don't have permission to use it")
                            ));
                        }
                        
                        MemoType memoType = memoTypeOpt.get();
                        LocalDateTime now = LocalDateTime.now();

                        // Create new Memo entity with proper relationships and timestamps
                        Memo newMemo = Memo.builder()
                            .name(memoDto.getName())
                            .detail(memoDto.getDetail())
                            .memoType(memoType)
                            .createdBy(user)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();

                        // Save to repository
                        return memoRepository.persist(newMemo)
                            .map(savedMemo -> Either.<ServiceError, Memo>right(savedMemo))
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, Memo>left(
                                    new UnexpectedError("Failed to create memo: " + throwable.getMessage(), throwable)
                                )
                            );
                    });
            });
    } // end newMemo

    // editMemo
    /*
     * TODO: Implement the method to edit an existing memo.
     * It should check if the memo exists and belong to this user by MemoRepository, validate the request,
     * validtae the memo type, and then update the memo using MemoRepository.
     * if the dto is empty or null it should not replace the old value
     * the saved memo should be returned
     */
    @WithTransaction
    public Uni<Either<ServiceError, Memo>> editMemo(UUID memoId, ReqCreateUpdateMemoDto memoDto, UUID userId) {
        // Validate input
        if (memoDto == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Memo data cannot be null")));
        }

        return memoRepository.findByMemoIdAndUserId(memoId, userId)
            .chain(memoOpt -> {
                if (memoOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Memo>left(
                        new ResourceNotFoundError("Memo not found with ID: " + memoId + " or you don't have permission to access it")
                    ));
                }
                
                Memo memo = memoOpt.get();
                
                // If memo type is being changed, validate it
                if (memoDto.getMemoType() != null) {
                    return memoTypeRepository.findByIdAndUserId(memoDto.getMemoType(), userId)
                        .chain(memoTypeOpt -> {
                            if (memoTypeOpt.isEmpty()) {
                                return Uni.createFrom().item(Either.<ServiceError, Memo>left(
                                    new ResourceNotFoundError("Memo type not found with ID: " + memoDto.getMemoType() + " or you don't have permission to use it")
                                ));
                            }
                            
                            MemoType memoType = memoTypeOpt.get();
                            
                            // Update fields only if they are not null or empty
                            updateMemoFields(memo, memoDto, memoType);
                            memo.setUpdatedAt(LocalDateTime.now());
                            
                            return memoRepository.persist(memo)
                                .map(updatedMemo -> Either.<ServiceError, Memo>right(updatedMemo))
                                .onFailure().recoverWithItem(throwable -> 
                                    Either.<ServiceError, Memo>left(
                                        new UnexpectedError("Failed to update memo: " + throwable.getMessage(), throwable)
                                    )
                                );
                        });
                } else {
                    // Update fields without changing memo type
                    updateMemoFields(memo, memoDto, null);
                    memo.setUpdatedAt(LocalDateTime.now());
                    
                    return memoRepository.persist(memo)
                        .map(updatedMemo -> Either.<ServiceError, Memo>right(updatedMemo))
                        .onFailure().recoverWithItem(throwable -> 
                            Either.<ServiceError, Memo>left(
                                new UnexpectedError("Failed to update memo: " + throwable.getMessage(), throwable)
                            )
                        );
                }
            });
    } // end editMemo

    // Helper method to update memo fields with null checks
    private void updateMemoFields(Memo memo, ReqCreateUpdateMemoDto memoDto, MemoType memoType) {
        if (memoDto.getName() != null && !memoDto.getName().trim().isEmpty()) {
            memo.setName(memoDto.getName().trim());
        }
        
        if (memoDto.getDetail() != null && !memoDto.getDetail().trim().isEmpty()) {
            memo.setDetail(memoDto.getDetail().trim());
        }
        
        
        if (memoType != null) {
            memo.setMemoType(memoType);
        }
    }

    // deleteMemo
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> deleteMemo(UUID memoId, UUID userId) {
        return memoRepository.findByMemoIdAndUserId(memoId, userId)
            .chain(memoOpt -> {
                if (memoOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ResourceNotFoundError("Memo not found with ID: " + memoId + " or you don't have permission to access it")
                    ));
                }
                
                Memo memo = memoOpt.get();
                
                return memoRepository.delete(memo)
                    .map(deleted -> Either.<ServiceError, Boolean>right(true))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, Boolean>left(
                            new UnexpectedError("Failed to delete memo: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    } // end deleteMemo

    // viewMemoById
    @WithSession
    public Uni<Either<ServiceError, Memo>> viewMemoById(UUID memoId, UUID userId) {
        return memoRepository.findByMemoIdAndUserId(memoId, userId)
            .map(memoOpt -> {
                if (memoOpt.isEmpty()) {
                    return Either.<ServiceError, Memo>left(
                        new ResourceNotFoundError("Memo not found with ID: " + memoId + " or you don't have permission to access it")
                    );
                }
                
                return Either.<ServiceError, Memo>right(memoOpt.get());
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, Memo>left(
                    new UnexpectedError("Failed to retrieve memo: " + throwable.getMessage(), throwable)
                )
            );
    } // end viewMemoById

    // viewAllUserMemo
    @WithSession
    public Uni<Either<ServiceError, List<Memo>>> viewAllUserMemos(UUID userId) {
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, List<Memo>>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }
                
                User user = userOpt.get();
                return memoRepository.find("createdBy", user).list()
                    .map(memos -> Either.<ServiceError, List<Memo>>right(memos))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, List<Memo>>left(
                            new UnexpectedError("Failed to retrieve user memos: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    } // end viewAllUserMemos

    // viewAllMemoByMemoTypeId
    @WithSession
    public Uni<Either<ServiceError, List<Memo>>> viewAllMemoByMemoTypeId(UUID memoTypeId, UUID userId) {
        return memoTypeRepository.findByIdAndUserId(memoTypeId, userId)
            .chain(memoTypeOpt -> {
                if (memoTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, List<Memo>>left(
                        new ResourceNotFoundError("Memo type not found with ID: " + memoTypeId + " or you don't have permission to access it")
                    ));
                }
                
                return memoRepository.findAllUserByMemoTypeId(memoTypeId, userId)
                    .map(result -> {
                        if (result.isLeft()) {
                            // Convert RepositoryError to ServiceError
                            return Either.<ServiceError, List<Memo>>left(
                                new UnexpectedError("Failed to retrieve memos by type: " + result.getLeft().message())
                            );
                        }
                        // Return the successful result
                        return Either.<ServiceError, List<Memo>>right(result.getRight());
                    })
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, List<Memo>>left(
                            new UnexpectedError("Failed to retrieve memos by memo type: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    } // end viewAllMemoByMemoTypeId

} // end class
