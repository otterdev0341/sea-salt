package com.otterdev.infrastructure.service.implService.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.ContactType;
import com.otterdev.domain.valueObject.dto.contactType.ReqCreateContactTypeDto;
import com.otterdev.domain.valueObject.dto.contactType.ReqUpdateContactTypeDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.ContactTypeRepository;
import com.otterdev.infrastructure.service.internal.base.InternalContactTypeService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;



@ApplicationScoped
@Named("contactTypeService")  // Add this qualifier
class ContactTypeServiceImpl implements InternalContactTypeService {

    private final ContactTypeRepository contactTypeRepository;

    @Inject
    public ContactTypeServiceImpl(ContactTypeRepository contactTypeRepository) {
        this.contactTypeRepository = contactTypeRepository;
    }


    @Override
    public Uni<Either<ServiceError, ContactType>> createContactType(ReqCreateContactTypeDto reqCreateContactTypeDto,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createContactType'");
    }

    @Override
    public Uni<Either<ServiceError, ContactType>> updateContactType(ReqUpdateContactTypeDto reqUpdateContactTypeDto,
            UUID contactTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateContactType'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteContactType(UUID contactTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteContactType'");
    }

    @Override
    public Uni<Either<ServiceError, ContactType>> getContactTypeById(UUID contactTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContactTypeById'");
    }

    @Override
    public Uni<Either<ServiceError, List<ContactType>>> getAllContactTypes(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllContactTypes'");
    }

    // Implement methods from ContactTypeService interface here
    // For example:
    // @Override
    // public Uni<Either<ServiceError, ContactType>> createContactType(ReqCreateContactTypeDto reqCreateContactTypeDto, UUID userId) {
    //     // Implementation logic
    // }
    
}
