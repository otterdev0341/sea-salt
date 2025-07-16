package com.otterdev.infrastructure.service.internal.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.Property;
import com.otterdev.domain.valueObject.dto.property.ReqCreatePropertyDto;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalPropertyService {
    
    // common case
    Uni<Either<ServiceError, Property>> createProperty(ReqCreatePropertyDto reqCreatePropertyDto, UUID userId);

    Uni<Either<ServiceError, Property>> updateProperty(ReqCreatePropertyDto reqCreatePropertyDto, UUID propertyId, UUID userId);

    Uni<Either<ServiceError, Property>> getPropertyById(UUID propertyId, UUID userId);

    Uni<Either<ServiceError, Boolean>> deleteProperty(UUID propertyId, UUID userId);

    Uni<Either<ServiceError, List<Property>>> getAllProperties(String slug, UUID userId);

    // query/filter case
    Uni<Either<ServiceError, List<Property>>> getPropertiesByType(UUID propertyTypeId, UUID userId);

    Uni<Either<ServiceError, List<Property>>> getPropertiesByStatus(UUID statusId, UUID userId);

    Uni<Either<ServiceError, List<Property>>> getPropertiesBySold(Boolean isSold, UUID userId);

    // relation case

}
