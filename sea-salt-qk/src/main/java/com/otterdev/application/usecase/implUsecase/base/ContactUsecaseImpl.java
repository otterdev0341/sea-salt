package com.otterdev.application.usecase.implUsecase.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.application.usecase.internal.base.InternalContactUsecase;
import com.otterdev.domain.entity.Contact;
import com.otterdev.domain.valueObject.dto.contact.ReqCreateContactDto;
import com.otterdev.domain.valueObject.dto.contact.ReqUpdateContactDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.base.InternalContactService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
class ContactUsecaseImpl implements InternalContactUsecase {

    private final InternalContactService contactService;
    

    @Inject
    public ContactUsecaseImpl(InternalContactService contactService) {
        this.contactService = contactService;
    }
    


    @Override
    public Uni<Either<UsecaseError, Contact>> createContact(ReqCreateContactDto reqCreateContactDto, UUID userId) {
        
        return contactService.createContact(reqCreateContactDto, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to create contact type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(result.getRight()))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, Contact>> updateContact(ReqUpdateContactDto reqUpdateContactDto, UUID contactId,
            UUID userId) {
        
        return contactService.updateContact(reqUpdateContactDto, contactId, userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to update contact type cause by : " + error.message()))), 
                success -> Uni.createFrom().item(Either.right(result.getRight()))
            ));
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteContact(UUID contactId, UUID userId) {
        
        return contactService.deleteContact(contactId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to delete contact type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, Contact>> getContactById(UUID contactId, UUID userId) {
        
        return contactService.getContactById(contactId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve contact type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, List<Contact>>> getAllContacts(UUID userId) {
        
        return contactService.getAllContacts(userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve contacts cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, List<Contact>>> getAllContactsByContactType(UUID contactTypeId, UUID userId) {
        
        return contactService.getAllContactsByContactType(contactTypeId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve contacts by contact type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }
    
}
