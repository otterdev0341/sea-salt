package com.otterdev.infrastructure.service.implService.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.PropertyStatus;
import com.otterdev.domain.valueObject.dto.propertyStatus.ReqCreatePropertyStatusDto;
import com.otterdev.domain.valueObject.dto.propertyStatus.ReqUpdatePropertyStatusDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.service.internal.base.InternalPropertyStatusService;
import com.spencerwi.either.Either;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class PropertyStatusImpl implements InternalPropertyStatusService {

    private final PropertyStatusRepositoryImpl propertyStatusRepository;

    @Inject
    public PropertyStatusImpl(PropertyStatusRepositoryImpl propertyStatusRepository) {
        this.propertyStatusRepository = propertyStatusRepository;
    }


    @Override
    public Either<ServiceError, PropertyStatus> createPropertyStatus(ReqCreatePropertyStatusDto reqCreatePropertyStatus,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPropertyStatus'");
    }

    @Override
    public Either<ServiceError, PropertyStatus> updatePropertyStatus(ReqUpdatePropertyStatusDto reqUpdatePropertyStatus,
            UUID propertyStatusId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePropertyStatus'");
    }

    @Override
    public Either<ServiceError, Boolean> deletePropertyStatus(UUID propertyStatusId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePropertyStatus'");
    }

    @Override
    public Either<ServiceError, PropertyStatus> getPropertyStatusById(UUID propertyStatusId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPropertyStatusById'");
    }

    @Override
    public Either<ServiceError, List<PropertyStatus>> getAllPropertyStatuses(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPropertyStatuses'");
    }
    
}
