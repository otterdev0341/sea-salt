package com.otterdev.infrastructure.service.implService.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.Property;
import com.otterdev.domain.valueObject.dto.property.ReqCreatePropertyDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.PropertyRepository;
import com.otterdev.infrastructure.service.internal.base.InternalPropertyService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class PropertyServiceImpl implements InternalPropertyService {

    private final PropertyRepository propertyRepository;

    @Inject
    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public Uni<Either<ServiceError, Property>> createProperty(ReqCreatePropertyDto reqCreatePropertyDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createProperty'");
    }

    @Override
    public Uni<Either<ServiceError, Property>> updateProperty(ReqCreatePropertyDto reqCreatePropertyDto,
            UUID propertyId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProperty'");
    }

    @Override
    public Uni<Either<ServiceError, Property>> getPropertyById(UUID propertyId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPropertyById'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteProperty(UUID propertyId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProperty'");
    }

    @Override
    public Uni<Either<ServiceError, List<Property>>> getAllProperties(String slug, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllProperties'");
    }

    @Override
    public Uni<Either<ServiceError, List<Property>>> getPropertiesByType(UUID propertyTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPropertiesByType'");
    }

    @Override
    public Uni<Either<ServiceError, List<Property>>> getPropertiesByStatus(UUID statusId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPropertiesByStatus'");
    }

    @Override
    public Uni<Either<ServiceError, List<Property>>> getPropertiesBySold(Boolean isSold, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPropertiesBySold'");
    }
    
}
