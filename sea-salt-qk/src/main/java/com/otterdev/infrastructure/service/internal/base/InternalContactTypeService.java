package com.otterdev.infrastructure.service.internal.base;

import java.util.List;
import java.util.UUID;
import com.otterdev.domain.entity.ContactType;
import com.otterdev.domain.valueObject.dto.contactType.ReqCreateContactTypeDto;
import com.otterdev.domain.valueObject.dto.contactType.ReqUpdateContactTypeDto;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalContactTypeService {
    
    // This class is intentionally left empty as a placeholder.
    // It can be implemented later with methods similar to PropertyStatusUsecase.
    
    // Example methods could include:
    // - createContactType
    // - updateContactType
    // - deleteContactType
    // - getContactTypeById
    // - getAllContactTypes

    @WithTransaction
    Uni<Either<ServiceError, ContactType>> createContactType(ReqCreateContactTypeDto reqCreateContactTypeDto, UUID userId); 
    
    @WithTransaction
    Uni<Either<ServiceError, ContactType>> updateContactType(ReqUpdateContactTypeDto reqUpdateContactTypeDto, UUID contactTypeId, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> deleteContactType(UUID contactTypeId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, ContactType>> getContactTypeById(UUID contactTypeId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, List<ContactType>>> getAllContactTypes(UUID userId);

}
