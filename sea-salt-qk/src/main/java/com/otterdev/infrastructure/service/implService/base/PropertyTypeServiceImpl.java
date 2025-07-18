package com.otterdev.infrastructure.service.implService.base;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.PropertyType;
import com.otterdev.domain.valueObject.dto.propertyType.ReqCreatePropertyTypeDto;
import com.otterdev.domain.valueObject.dto.propertyType.ReqUpdatePropertyTypeDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.PropertyTypeRepository;
import com.otterdev.infrastructure.repository.UserRepository;
import com.otterdev.infrastructure.service.internal.base.InternalPropertyTypeService;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@ApplicationScoped
@Named("propertyTypeService")
class PropertyTypeServiceImpl implements InternalPropertyTypeService {

    private final PropertyTypeRepository propertyTypeRepository;
    private final UserRepository userRepository;

    @Inject
    public PropertyTypeServiceImpl(PropertyTypeRepository propertyTypeRepository, UserRepository userRepository) {
        this.propertyTypeRepository = propertyTypeRepository;
        this.userRepository = userRepository;
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, PropertyType>> createPropertyType(
            ReqCreatePropertyTypeDto reqCreatePropertyTypeDto,
            UUID userId) {
        
        PropertyType newPropertyType = new PropertyType();
        LocalDateTime now = LocalDateTime.now();

        return propertyTypeRepository.isExistByDetailAndUserId(reqCreatePropertyTypeDto.getDetail().trim(), userId)
            .chain(isExist -> {
                if (isExist) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.DuplicateEntry("Property type already exists"))
                    );
                }
                return userRepository.findByUserId(userId)
                    .chain(userOpt -> {
                        if (userOpt.isEmpty()) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.NotFound("User not found"))
                            );
                        }
                        newPropertyType.setDetail(reqCreatePropertyTypeDto.getDetail().trim());
                        newPropertyType.setCreatedBy(userOpt.get());
                        newPropertyType.setCreatedAt(now);
                        newPropertyType.setUpdatedAt(now);

                        return propertyTypeRepository.persist(newPropertyType)
                            .map(persistedPropertyType -> Either.right(persistedPropertyType));
                    });
            });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, PropertyType>> updatePropertyType(
            UUID propertyTypeId,
            ReqUpdatePropertyTypeDto reqUpdatePropertyTypeDto,
            UUID userId) {
        
        return propertyTypeRepository.findByIdAndUserId(propertyTypeId, userId)
            .chain(propertyTypeOpt -> {
                if (propertyTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Property type not found"))
                    );
                }

                PropertyType existingPropertyType = propertyTypeOpt.get();
                String newDetail = reqUpdatePropertyTypeDto.getDetail().trim();

                if (newDetail.equals(existingPropertyType.getDetail())) {
                    return Uni.createFrom().item(Either.right(existingPropertyType));
                }

                return propertyTypeRepository.isExistByDetailAndUserId(newDetail, userId)
                    .chain(exists -> {
                        if (exists) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.DuplicateEntry("Property type with this detail already exists"))
                            );
                        }

                        existingPropertyType.setDetail(newDetail);
                        existingPropertyType.setUpdatedAt(LocalDateTime.now());

                        return propertyTypeRepository.updatePropertyType(existingPropertyType, userId)
                            .chain(updatedResult -> {
                                if (updatedResult.isLeft()) {
                                    return Uni.createFrom().item(
                                        Either.left(new ServiceError.OperationFailed(
                                            "Failed to update property type: " + updatedResult.getLeft().message()
                                        ))
                                    );
                                }
                                return propertyTypeRepository.findByIdAndUserId(existingPropertyType.getId(), userId)
                                    .map(propertyType -> Either.right(propertyType.orElse(null)));
                            });
                    });
            });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Void>> deletePropertyType(UUID propertyTypeId, UUID userId) {
        return propertyTypeRepository.findByIdAndUserId(propertyTypeId, userId)
            .chain(propertyTypeOpt -> {
                if (propertyTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Property type not found"))
                    );
                }

                return propertyTypeRepository.deletePropertyType(propertyTypeId, userId)
                    .chain(result -> {
                        if (result.isLeft()) {
                            return Uni.createFrom().item(
                                Either.<ServiceError, Void>left(new ServiceError.OperationFailed(
                                    "Failed to delete property type with id: " + propertyTypeId
                                ))
                            );
                        }
                        return Uni.createFrom().item(Either.<ServiceError, Void>right(null));
                    });
            });
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, PropertyType>> getPropertyTypeById(UUID propertyTypeId, UUID userId) {
        return propertyTypeRepository.findByIdAndUserId(propertyTypeId, userId)
            .chain(propertyTypeOpt -> {
                if (propertyTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Property type not found"))
                    );
                }
                return Uni.createFrom().item(Either.right(propertyTypeOpt.get()));
            });
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<PropertyType>>> getAllPropertyType(UUID userId) {
        return propertyTypeRepository.findAllByUserId(userId)
            .chain(result -> {
                if (result.isLeft()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.OperationFailed(
                            "Failed to retrieve property types: " + result.getLeft().message()
                        ))
                    );
                }
                return Uni.createFrom().item(Either.right(result.getRight()));
            });
    }
}
