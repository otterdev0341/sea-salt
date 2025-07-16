package com.otterdev.infrastructure.service.internal.base;

import java.util.UUID;

import com.otterdev.domain.entity.PropertyType;
import com.otterdev.domain.valueObject.dto.propertyType.ReqCreatePropertyTypeDto;
import com.otterdev.domain.valueObject.dto.propertyType.ReqUpdatePropertyTypeDto;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalPropertyTypeService {
    
    @WithTransaction
    Uni<Either<ServiceError, PropertyType>> createPropertyType(ReqCreatePropertyTypeDto reqCreatePropertyTypeDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, PropertyType>> updatePropertyType(UUID propertyTypeId, ReqUpdatePropertyTypeDto reqUpdatePropertyTypeDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Void>> deletePropertyType(UUID propertyTypeId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, PropertyType>> getPropertyTypeById(UUID propertyTypeId);

    @WithSession
    Uni<Either<ServiceError, PropertyType>> getPropertyTypeByDetail(String detail);

}
