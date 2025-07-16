package com.otterdev.application.usecase.implUsecase.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.application.usecase.internal.base.InternalContactUsecase;
import com.otterdev.domain.entity.Contact;
import com.otterdev.domain.valueObject.dto.contact.ReqCreateContactDto;
import com.otterdev.domain.valueObject.dto.contact.ReqUpdateContactDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.base.InternalContactService;
import com.otterdev.infrastructure.service.internal.base.InternalContactTypeService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
class ContactUsecaseImpl implements InternalContactUsecase {

    private final InternalContactService contactService;
    private final InternalContactTypeService contactTypeService;

    @Inject
    public ContactUsecaseImpl(InternalContactService contactService, InternalContactTypeService contactTypeService) {
        this.contactService = contactService;
        this.contactTypeService = contactTypeService;
    }


    @Override
    public Uni<Either<UsecaseError, Contact>> createContact(ReqCreateContactDto reqCreateContactDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createContact'");
    }

    @Override
    public Uni<Either<UsecaseError, Contact>> updateContact(ReqUpdateContactDto reqUpdateContactDto, UUID contactId,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateContact'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteContact(UUID contactId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteContact'");
    }

    @Override
    public Uni<Either<UsecaseError, Contact>> getContactById(UUID contactId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContactById'");
    }

    @Override
    public Uni<Either<UsecaseError, List<Contact>>> getAllContacts(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllContacts'");
    }

    @Override
    public Uni<Either<UsecaseError, List<Contact>>> getAllContactsByContactType(UUID contactTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllContactsByContactType'");
    }
    
}
