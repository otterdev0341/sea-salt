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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createContactType'");
        
    }

    @Override
    public Uni<Either<UsecaseError, ContactType>> updateContactType(ReqUpdateContactTypeDto reqUpdateContactTypeDto,
            UUID contactTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateContactType'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteContactType(UUID contactTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteContactType'");
    }

    @Override
    public Uni<Either<UsecaseError, ContactType>> getContactTypeById(UUID contactTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContactTypeById'");
    }

    @Override
    public Uni<Either<UsecaseError, List<ContactType>>> getAllContactTypes(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllContactTypes'");
    }

   
    
}
