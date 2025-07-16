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
            ReqCreatePropertyStatusDto reqCreatePropertyStatus, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPropertyStatus'");
    }

    @Override
    public Uni<Either<UsecaseError, PropertyStatus>> updatePropertyStatus(
            ReqUpdatePropertyStatusDto reqUpdatePropertyStatus, UUID propertyStatusId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePropertyStatus'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deletePropertyStatus(UUID propertyStatusId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePropertyStatus'");
    }

    @Override
    public Uni<Either<UsecaseError, PropertyStatus>> getPropertyStatusById(UUID propertyStatusId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPropertyStatusById'");
    }

    @Override
    public Uni<Either<UsecaseError, List<PropertyStatus>>> getAllPropertyStatuses(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPropertyStatuses'");
    }


    
    
}
