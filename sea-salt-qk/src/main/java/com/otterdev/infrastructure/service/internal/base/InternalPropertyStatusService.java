package com.otterdev.infrastructure.service.internal.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.PropertyStatus;
import com.otterdev.domain.valueObject.dto.propertyStatus.ReqCreatePropertyStatusDto;
import com.otterdev.domain.valueObject.dto.propertyStatus.ReqUpdatePropertyStatusDto;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("propertyStatusService")
public interface InternalPropertyStatusService {
    
    
    @WithTransaction
    Uni<Either<ServiceError, PropertyStatus>> createPropertyStatus(ReqCreatePropertyStatusDto reqCreatePropertyStatus, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, PropertyStatus>> updatePropertyStatus(ReqUpdatePropertyStatusDto reqUpdatePropertyStatus, UUID propertyStatusId ,UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> deletePropertyStatus(UUID propertyStatusId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, PropertyStatus>> getPropertyStatusById(UUID propertyStatusId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, List<PropertyStatus>>> getAllPropertyStatuses(UUID userId);

}
