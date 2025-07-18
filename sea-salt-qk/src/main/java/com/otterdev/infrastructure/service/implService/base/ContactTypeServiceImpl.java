package com.otterdev.infrastructure.service.implService.base;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.otterdev.domain.entity.ContactType;
import com.otterdev.domain.valueObject.dto.contactType.ReqCreateContactTypeDto;
import com.otterdev.domain.valueObject.dto.contactType.ReqUpdateContactTypeDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.ContactTypeRepository;
import com.otterdev.infrastructure.repository.UserRepository;
import com.otterdev.infrastructure.service.internal.base.InternalContactTypeService;
import com.spencerwi.either.Either;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;



@ApplicationScoped
@Named("contactTypeService")  // Add this qualifier
class ContactTypeServiceImpl implements InternalContactTypeService {

    private final ContactTypeRepository contactTypeRepository;
    private final UserRepository userRepository;

    @Inject
    public ContactTypeServiceImpl(ContactTypeRepository contactTypeRepository, UserRepository userRepository) {
        this.contactTypeRepository = contactTypeRepository;
        this.userRepository = userRepository;
    }
    


    @Override
    @WithTransaction
    public Uni<Either<ServiceError, ContactType>> createContactType(ReqCreateContactTypeDto reqCreateContactTypeDto,
            UUID userId) {
        
        ContactType newContactType = new ContactType();
        LocalDateTime now = LocalDateTime.now();

        return contactTypeRepository.isExistByDetailAndUserId(reqCreateContactTypeDto.getDetail().trim(), userId)
            .chain(isExist -> {
                if (isExist) {
                    ServiceError error = new ServiceError.DuplicateEntry("Contact type already exists");
                    return Uni.createFrom().item(Either.left(error));
                } 
                return userRepository.findByUserId(userId)
                    .chain(userOpt -> {
                        if (userOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                        }
                        newContactType.setDetail(reqCreateContactTypeDto.getDetail().trim());
                        newContactType.setCreatedBy(userOpt.get());
                        newContactType.setCreatedAt(now);
                        newContactType.setUpdatedAt(now);
                        
                        return contactTypeRepository.persist(newContactType)
                            .map(persistedContactType -> Either.right(persistedContactType));
                    });
            });

    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, ContactType>> updateContactType(
            ReqUpdateContactTypeDto reqUpdateContactTypeDto,
            UUID contactTypeId, 
            UUID userId) {
        
        return contactTypeRepository.findByIdAndUserId(contactTypeId, userId)
            .chain(contactTypeOpt -> {
                if (contactTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Contact type not found"))
                    );
                }

                ContactType existingContactType = contactTypeOpt.get();
                String newDetail = reqUpdateContactTypeDto.getDetail().trim();

                // Check if the new detail is the same as current
                if (newDetail.equals(existingContactType.getDetail())) {
                    return Uni.createFrom().item(
                        Either.right(existingContactType)
                    );
                }

                // Check if detail already exists for another contact type
                return contactTypeRepository.isExistByDetailAndUserId(newDetail, userId)
                    .chain(exists -> {
                        if (exists) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.DuplicateEntry("Contact type with this detail already exists"))
                            );
                        }

                        // Update the contact type
                        existingContactType.setDetail(newDetail);
                        existingContactType.setUpdatedAt(LocalDateTime.now());

                        // Persist and fetch fresh copy with all relations
                        return contactTypeRepository.updateContactType(existingContactType, userId)
                            .chain(updatedResult -> {
                                if (updatedResult.isLeft()) {
                                    return Uni.createFrom().item(
                                        Either.left(new ServiceError.OperationFailed(
                                            "Failed to update contact type: " + updatedResult.getLeft().message()
                                        ))
                                    );
                                }
                                return contactTypeRepository.findByIdAndUserId(existingContactType.getId(), userId)
                                    .map(contactType -> Either.right(contactType.orElse(null)));
                            });
                    });
            });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> deleteContactType(UUID contactTypeId, UUID userId) {
        
        return contactTypeRepository.findByIdAndUserId(contactTypeId, userId)
            .chain(contactTypeOpt -> {
                if (contactTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Contact type not found"))
                    );
                }

                return contactTypeRepository.deleteContactType(contactTypeId, userId)
                        .chain(result -> result.fold(
                            error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to delete contact type: " + error.message()))),
                            success -> Uni.createFrom().item(Either.right(success))
                        ));
            });
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, ContactType>> getContactTypeById(UUID contactTypeId, UUID userId) {
        
        return contactTypeRepository.findByIdAndUserId(contactTypeId, userId)
            .chain(contactTypeOpt -> {
                if (contactTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Contact type not found"))
                    );
                }
                return Uni.createFrom().item(Either.right(contactTypeOpt.get()));
            });
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<ContactType>>> getAllContactTypes(UUID userId) {
        
        return contactTypeRepository.findAllByUserId(userId)
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
