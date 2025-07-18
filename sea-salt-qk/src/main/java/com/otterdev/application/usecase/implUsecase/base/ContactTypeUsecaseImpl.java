package com.otterdev.application.usecase.implUsecase.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.application.usecase.internal.base.InternalContactTypeUsecase;
import com.otterdev.domain.entity.ContactType;
import com.otterdev.domain.valueObject.dto.contactType.ReqCreateContactTypeDto;
import com.otterdev.domain.valueObject.dto.contactType.ReqUpdateContactTypeDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.base.InternalContactTypeService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
class ContactTypeUsecaseImpl implements InternalContactTypeUsecase {

    private final InternalContactTypeService contactTypeService;

    @Inject
    public ContactTypeUsecaseImpl(InternalContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }

    @Override
    public Uni<Either<UsecaseError, ContactType>> createContactType(ReqCreateContactTypeDto reqCreateContactTypeDto,
            UUID userId) {
        
        return contactTypeService.createContactType(reqCreateContactTypeDto, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to create contact type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(result.getRight()))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, ContactType>> updateContactType(ReqUpdateContactTypeDto reqUpdateContactTypeDto,
            UUID contactTypeId, UUID userId) {

        return contactTypeService.updateContactType(reqUpdateContactTypeDto, contactTypeId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to update contact type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(result.getRight()))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteContactType(UUID contactTypeId, UUID userId) {
        
        return contactTypeService.deleteContactType(contactTypeId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to delete contact type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, ContactType>> getContactTypeById(UUID contactTypeId, UUID userId) {
        
        return contactTypeService.getContactTypeById(contactTypeId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve contact type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, List<ContactType>>> getAllContactTypes(UUID userId) {
        
        return contactTypeService.getAllContactTypes(userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve contact types cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

   
    
}
