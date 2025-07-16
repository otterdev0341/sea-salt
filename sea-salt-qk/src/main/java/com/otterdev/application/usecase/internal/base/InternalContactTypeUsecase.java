package com.otterdev.application.usecase.internal.base;

import java.util.List;
import java.util.UUID;
import com.otterdev.domain.entity.ContactType;
import com.otterdev.domain.valueObject.dto.contactType.ReqCreateContactTypeDto;
import com.otterdev.domain.valueObject.dto.contactType.ReqUpdateContactTypeDto;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalContactTypeUsecase {
    
    // This class is intentionally left empty as a placeholder.
    // It can be implemented later with methods similar to PropertyStatusUsecase.
    
    // Example methods could include:
    // - createContactType
    // - updateContactType
    // - deleteContactType
    // - getContactTypeById
    // - getAllContactTypes

    @WithTransaction
    Uni<Either<UsecaseError, ContactType>> createContactType(ReqCreateContactTypeDto reqCreateContactTypeDto, UUID userId); 
    
    @WithTransaction
    Uni<Either<UsecaseError, ContactType>> updateContactType(ReqUpdateContactTypeDto reqUpdateContactTypeDto, UUID contactTypeId, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deleteContactType(UUID contactTypeId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, ContactType>> getContactTypeById(UUID contactTypeId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, List<ContactType>>> getAllContactTypes(UUID userId);

}
