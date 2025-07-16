package com.otterdev.application.usecase.internal.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.Property;
import com.otterdev.domain.valueObject.dto.property.ReqCreatePropertyDto;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalPropertyUsecase {
    
    // common case
    Uni<Either<UsecaseError, Property>> createProperty(ReqCreatePropertyDto reqCreatePropertyDto, UUID userId);

    Uni<Either<UsecaseError, Property>> updateProperty(ReqCreatePropertyDto reqCreatePropertyDto, UUID propertyId, UUID userId);

    Uni<Either<UsecaseError, Property>> getPropertyById(UUID propertyId, UUID userId);

    Uni<Either<UsecaseError, Boolean>> deleteProperty(UUID propertyId, UUID userId);

    Uni<Either<UsecaseError, List<Property>>> getAllProperties(String slug, UUID userId);

    // query/filter case
    Uni<Either<UsecaseError, List<Property>>> getPropertiesByType(UUID propertyTypeId, UUID userId);

    Uni<Either<UsecaseError, List<Property>>> getPropertiesByStatus(UUID statusId, UUID userId);

    Uni<Either<UsecaseError, List<Property>>> getPropertiesBySold(Boolean isSold, UUID userId);

    // relation case

}
