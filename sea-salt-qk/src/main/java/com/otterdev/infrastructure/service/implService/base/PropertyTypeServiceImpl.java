package com.otterdev.infrastructure.service.implService.base;

import java.util.UUID;

import com.otterdev.domain.entity.PropertyType;
import com.otterdev.domain.valueObject.dto.propertyType.ReqCreatePropertyTypeDto;
import com.otterdev.domain.valueObject.dto.propertyType.ReqUpdatePropertyTypeDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.PropertyTypeRepository;
import com.otterdev.infrastructure.service.internal.base.InternalPropertyTypeService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@ApplicationScoped
@Named("propertyTypeService")  // Add this qualifier
class PropertyTypeServiceImpl implements InternalPropertyTypeService{

    private final PropertyTypeRepository propertyTypeRepository;

    @Inject
    public PropertyTypeServiceImpl(PropertyTypeRepository propertyTypeRepository) {
        this.propertyTypeRepository = propertyTypeRepository;
    }


    @Override
    public Uni<Either<ServiceError, PropertyType>> createPropertyType(ReqCreatePropertyTypeDto reqCreatePropertyTypeDto,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPropertyType'");
    }

    @Override
    public Uni<Either<ServiceError, PropertyType>> updatePropertyType(UUID propertyTypeId,
            ReqUpdatePropertyTypeDto reqUpdatePropertyTypeDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePropertyType'");
    }

    @Override
    public Uni<Either<ServiceError, Void>> deletePropertyType(UUID propertyTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePropertyType'");
    }

    @Override
    public Uni<Either<ServiceError, PropertyType>> getPropertyTypeById(UUID propertyTypeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPropertyTypeById'");
    }

    @Override
    public Uni<Either<ServiceError, PropertyType>> getPropertyTypeByDetail(String detail) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPropertyTypeByDetail'");
    }
    
}
