package com.otterdev.infrastructure.service.implService.base;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.PropertyStatus;
import com.otterdev.domain.valueObject.dto.propertyStatus.ReqCreatePropertyStatusDto;
import com.otterdev.domain.valueObject.dto.propertyStatus.ReqUpdatePropertyStatusDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.PropertyStatusRepository;
import com.otterdev.infrastructure.repository.UserRepository;
import com.otterdev.infrastructure.service.internal.base.InternalPropertyStatusService;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("propertyStatusService")
class PropertyStatusServiceImpl implements InternalPropertyStatusService {

    private final PropertyStatusRepository propertyStatusRepository;
    private final UserRepository userRepository;

    @Inject
    public PropertyStatusServiceImpl(PropertyStatusRepository propertyStatusRepository, UserRepository userRepository) {
        this.propertyStatusRepository = propertyStatusRepository;
        this.userRepository = userRepository;
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, PropertyStatus>> createPropertyStatus(
            ReqCreatePropertyStatusDto reqCreatePropertyStatus, UUID userId) {

        PropertyStatus newPropertyStatus = new PropertyStatus();
        LocalDateTime now = LocalDateTime.now();

        return propertyStatusRepository.isExistByDetailAndUserId(reqCreatePropertyStatus.getDetail().trim(), userId)
            .chain(isExist -> {
                if (isExist) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.DuplicateEntry("Property status already exists"))
                    );
                }
                return userRepository.findByUserId(userId)
                    .chain(userOpt -> {
                        if (userOpt.isEmpty()) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.NotFound("User not found"))
                            );
                        }
                        newPropertyStatus.setDetail(reqCreatePropertyStatus.getDetail().trim());
                        newPropertyStatus.setCreatedBy(userOpt.get());
                        newPropertyStatus.setCreatedAt(now);
                        newPropertyStatus.setUpdatedAt(now);

                        return propertyStatusRepository.persist(newPropertyStatus)
                            .map(persistedPropertyStatus -> Either.right(persistedPropertyStatus));
                    });
            });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, PropertyStatus>> updatePropertyStatus(
            ReqUpdatePropertyStatusDto reqUpdatePropertyStatus, UUID propertyStatusId, UUID userId) {

        return propertyStatusRepository.findByIdAndUserId(propertyStatusId, userId)
            .chain(propertyStatusOpt -> {
                if (propertyStatusOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Property status not found"))
                    );
                }

                PropertyStatus existingPropertyStatus = propertyStatusOpt.get();
                String newDetail = reqUpdatePropertyStatus.getDetail().trim();

                if (newDetail.equals(existingPropertyStatus.getDetail())) {
                    return Uni.createFrom().item(Either.right(existingPropertyStatus));
                }

                return propertyStatusRepository.isExistByDetailAndUserId(newDetail, userId)
                    .chain(exists -> {
                        if (exists) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.DuplicateEntry("Property status with this detail already exists"))
                            );
                        }

                        existingPropertyStatus.setDetail(newDetail);
                        existingPropertyStatus.setUpdatedAt(LocalDateTime.now());

                        return propertyStatusRepository.updatePropertyStatus(existingPropertyStatus, userId)
                            .chain(updatedResult -> {
                                if (updatedResult.isLeft()) {
                                    return Uni.createFrom().item(
                                        Either.left(new ServiceError.OperationFailed(
                                            "Failed to update property status: " + updatedResult.getLeft().message()
                                        ))
                                    );
                                }
                                return propertyStatusRepository.findByIdAndUserId(existingPropertyStatus.getId(), userId)
                                    .map(propertyStatus -> Either.right(propertyStatus.orElse(null)));
                            });
                    });
            });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> deletePropertyStatus(UUID propertyStatusId, UUID userId) {

        return propertyStatusRepository.findByIdAndUserId(propertyStatusId, userId)
            .chain(propertyStatusOpt -> {
                if (propertyStatusOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Property status not found"))
                    );
                }

                return propertyStatusRepository.deletePropertyStatus(propertyStatusId, userId)
                    .chain(result -> {
                        if (result.isLeft()) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.OperationFailed(
                                    "Failed to delete property status with id: " + propertyStatusId
                                ))
                            );
                        }
                        return Uni.createFrom().item(Either.right(true));
                    });
            });
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, PropertyStatus>> getPropertyStatusById(UUID propertyStatusId, UUID userId) {

        return propertyStatusRepository.findByIdAndUserId(propertyStatusId, userId)
            .chain(propertyStatusOpt -> {
                if (propertyStatusOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Property status not found"))
                    );
                }
                return Uni.createFrom().item(Either.right(propertyStatusOpt.get()));
            });
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<PropertyStatus>>> getAllPropertyStatuses(UUID userId) {

        return propertyStatusRepository.findAllByUserId(userId)
            .chain(result -> {
                if (result.isLeft()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.OperationFailed(
                            "Failed to retrieve property statuses: " + result.getLeft().message()
                        ))
                    );
                }
                return Uni.createFrom().item(Either.right(result.getRight()));
            });
    }
}
