package com.otterdev.infrastructure.service.internal.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.Contact;
import com.otterdev.domain.valueObject.dto.contact.ReqCreateContactDto;
import com.otterdev.domain.valueObject.dto.contact.ReqUpdateContactDto;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalContactService {
    
    @WithTransaction
    Uni<Either<ServiceError, Contact>> createContact(ReqCreateContactDto reqCreateContactDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Contact>> updateContact(ReqUpdateContactDto reqUpdateContactDto, UUID contactId, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> deleteContact(UUID contactId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, Contact>> getContactById(UUID contactId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, List<Contact>>> getAllContacts(UUID userId);

    @WithSession
    Uni<Either<ServiceError, List<Contact>>> getAllContactsByContactType(UUID contactTypeId, UUID userId);

}
