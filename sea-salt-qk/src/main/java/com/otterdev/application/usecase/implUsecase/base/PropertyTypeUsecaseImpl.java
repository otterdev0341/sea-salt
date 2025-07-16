package com.otterdev.application.usecase.implUsecase.base;

import java.util.UUID;

import com.otterdev.application.usecase.internal.base.InternalPropertyTypeUsecase;
import com.otterdev.domain.entity.PropertyType;
import com.otterdev.domain.valueObject.dto.propertyType.ReqCreatePropertyTypeDto;
import com.otterdev.domain.valueObject.dto.propertyType.ReqUpdatePropertyTypeDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.base.InternalPropertyTypeService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
class PropertyTypeUsecaseImpl implements InternalPropertyTypeUsecase {

    private final InternalPropertyTypeService propertyTypeService;

    @Inject
    public PropertyTypeUsecaseImpl(InternalPropertyTypeService propertyTypeService) {
        this.propertyTypeService = propertyTypeService;
    }

    @Override
    public Uni<Either<UsecaseError, PropertyType>> createPropertyType(ReqCreatePropertyTypeDto reqCreatePropertyTypeDto,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPropertyType'");
    }

    @Override
    public Uni<Either<UsecaseError, PropertyType>> updatePropertyType(UUID propertyTypeId,
            ReqUpdatePropertyTypeDto reqUpdatePropertyTypeDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePropertyType'");
    }

    @Override
    public Uni<Either<UsecaseError, Void>> deletePropertyType(UUID propertyTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePropertyType'");
    }

    @Override
    public Uni<Either<UsecaseError, PropertyType>> getPropertyTypeById(UUID propertyTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPropertyTypeById'");
    }

    @Override
    public Uni<Either<UsecaseError, PropertyType>> getAllPropertyType(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPropertyType'");
    }


    
}
