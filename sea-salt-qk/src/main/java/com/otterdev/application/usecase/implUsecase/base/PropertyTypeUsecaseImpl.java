package com.otterdev.application.usecase.implUsecase.base;

import java.util.List;
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
        
                return propertyTypeService.createPropertyType(reqCreatePropertyTypeDto, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to create property type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, PropertyType>> updatePropertyType(UUID propertyTypeId,
            ReqUpdatePropertyTypeDto reqUpdatePropertyTypeDto, UUID userId) {
        
        return propertyTypeService.updatePropertyType(propertyTypeId, reqUpdatePropertyTypeDto, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to update property type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, Void>> deletePropertyType(UUID propertyTypeId, UUID userId) {
        
        return propertyTypeService.deletePropertyType(propertyTypeId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to delete property type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, PropertyType>> getPropertyTypeById(UUID propertyTypeId, UUID userId) {
        
        return propertyTypeService.getPropertyTypeById(propertyTypeId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve property type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, List<PropertyType>>> getAllPropertyType(UUID userId) {
        
        return propertyTypeService.getAllPropertyType(userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve contact types cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }


    
}
