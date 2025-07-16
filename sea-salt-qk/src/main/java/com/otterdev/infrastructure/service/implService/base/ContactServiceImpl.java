package com.otterdev.infrastructure.service.implService.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.Contact;
import com.otterdev.domain.valueObject.dto.contact.ReqCreateContactDto;
import com.otterdev.domain.valueObject.dto.contact.ReqUpdateContactDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.ContactRepository;
import com.otterdev.infrastructure.service.internal.base.InternalContactService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class ContactServiceImpl implements InternalContactService {

    private final ContactRepository contactRepository;

    @Inject
    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Uni<Either<ServiceError, Contact>> createContact(ReqCreateContactDto reqCreateContactDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createContact'");
    }

    @Override
    public Uni<Either<ServiceError, Contact>> updateContact(ReqUpdateContactDto reqUpdateContactDto, UUID contactId,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateContact'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteContact(UUID contactId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteContact'");
    }

    @Override
    public Uni<Either<ServiceError, Contact>> getContactById(UUID contactId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContactById'");
    }

    @Override
    public Uni<Either<ServiceError, List<Contact>>> getAllContacts(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllContacts'");
    }

    @Override
    public Uni<Either<ServiceError, List<Contact>>> getAllContactsByContactType(UUID contactTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllContactsByContactType'");
    }
    
}
