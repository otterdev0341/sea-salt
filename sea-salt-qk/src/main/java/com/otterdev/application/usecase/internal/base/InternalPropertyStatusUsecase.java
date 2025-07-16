package com.otterdev.application.usecase.internal.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.PropertyStatus;
import com.otterdev.domain.valueObject.dto.propertyStatus.ReqCreatePropertyStatusDto;
import com.otterdev.domain.valueObject.dto.propertyStatus.ReqUpdatePropertyStatusDto;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalPropertyStatusUsecase {
    
    
    @WithTransaction
    Uni<Either<UsecaseError, PropertyStatus>> createPropertyStatus(ReqCreatePropertyStatusDto reqCreatePropertyStatus, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, PropertyStatus>> updatePropertyStatus(ReqUpdatePropertyStatusDto reqUpdatePropertyStatus, UUID propertyStatusId ,UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deletePropertyStatus(UUID propertyStatusId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, PropertyStatus>> getPropertyStatusById(UUID propertyStatusId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, List<PropertyStatus>>> getAllPropertyStatuses(UUID userId);

}
