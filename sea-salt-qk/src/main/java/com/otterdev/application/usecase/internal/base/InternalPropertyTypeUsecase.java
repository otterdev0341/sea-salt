package com.otterdev.application.usecase.internal.base;

import java.util.UUID;

import com.otterdev.domain.entity.PropertyType;
import com.otterdev.domain.valueObject.dto.propertyType.ReqCreatePropertyTypeDto;
import com.otterdev.domain.valueObject.dto.propertyType.ReqUpdatePropertyTypeDto;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalPropertyTypeUsecase {
    
    @WithTransaction
    Uni<Either<UsecaseError, PropertyType>> createPropertyType(ReqCreatePropertyTypeDto reqCreatePropertyTypeDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, PropertyType>> updatePropertyType(UUID propertyTypeId, ReqUpdatePropertyTypeDto reqUpdatePropertyTypeDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Void>> deletePropertyType(UUID propertyTypeId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, PropertyType>> getPropertyTypeById(UUID propertyTypeId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, PropertyType>> getAllPropertyType(UUID userId);

}
