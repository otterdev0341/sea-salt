package com.otterdev.application.usecase.implUsecase.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.application.usecase.internal.base.InternalPropertyStatusUsecase;
import com.otterdev.domain.entity.PropertyStatus;
import com.otterdev.domain.valueObject.dto.propertyStatus.ReqCreatePropertyStatusDto;
import com.otterdev.domain.valueObject.dto.propertyStatus.ReqUpdatePropertyStatusDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.base.InternalPropertyStatusService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
class PropertyStatusUsecaseImpl implements InternalPropertyStatusUsecase {

    private final InternalPropertyStatusService propertyStatusService;

    @Inject
    public PropertyStatusUsecaseImpl(InternalPropertyStatusService propertyStatusService) {
        this.propertyStatusService = propertyStatusService;
    }

    @Override
    public Uni<Either<UsecaseError, PropertyStatus>> createPropertyStatus(
            ReqCreatePropertyStatusDto reqCreatePropertyStatus, 
            UUID userId) {
        
        return propertyStatusService.createPropertyStatus(reqCreatePropertyStatus, userId)
            .chain(result -> {
                if (result.isLeft()) {
                    return Uni.createFrom().item(Either.left(new UsecaseError.BusinessError(
                        "Failed to create property status cause by : " + result.getLeft().message()
                    )));
                }
                return Uni.createFrom().item(Either.right(result.getRight()));
            });
        
                
    }

    @Override
    public Uni<Either<UsecaseError, PropertyStatus>> updatePropertyStatus(
            ReqUpdatePropertyStatusDto reqUpdatePropertyStatus, UUID propertyStatusId, UUID userId) {
        
        return propertyStatusService.updatePropertyStatus(reqUpdatePropertyStatus, propertyStatusId, userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError(
                    "Failed to update property status cause by : " + error.message()
                ))), 
                success -> Uni.createFrom().item(Either.right(success))
            ));
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deletePropertyStatus(UUID propertyStatusId, UUID userId) {
        
        return propertyStatusService.deletePropertyStatus(propertyStatusId, userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError(
                    "Failed to delete property status cause by : " + error.message()
                ))), 
                success -> Uni.createFrom().item(Either.right(success))
            ));
    }

    @Override
    public Uni<Either<UsecaseError, PropertyStatus>> getPropertyStatusById(UUID propertyStatusId, UUID userId) {
        
        return propertyStatusService.getPropertyStatusById(propertyStatusId, userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError(
                    "Failed to retrieve property status cause by : " + error.message()
                ))), 
                success -> Uni.createFrom().item(Either.right(success))
            ));
    }

    @Override
    public Uni<Either<UsecaseError, List<PropertyStatus>>> getAllPropertyStatuses(UUID userId) {
        
        return propertyStatusService.getAllPropertyStatuses(userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError(
                    "Failed to retrieve property statuses cause by : " + error.message()
                ))), 
                success -> Uni.createFrom().item(Either.right(success))
            ));
    }


    
    
}
