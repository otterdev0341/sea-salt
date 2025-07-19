package com.otterdev.infrastructure.service.implService.base;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.Contact;
import com.otterdev.domain.valueObject.dto.contact.ReqCreateContactDto;
import com.otterdev.domain.valueObject.dto.contact.ReqUpdateContactDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.ContactRepository;
import com.otterdev.infrastructure.repository.ContactTypeRepository;
import com.otterdev.infrastructure.repository.UserRepository;
import com.otterdev.infrastructure.service.internal.base.InternalContactService;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("contactService")  // Add this qualifier
class ContactServiceImpl implements InternalContactService {

    private final ContactRepository contactRepository;
    private final ContactTypeRepository contactTypeRepository;
    private final UserRepository userRepository;

    @Inject
    public ContactServiceImpl(ContactRepository contactRepository, ContactTypeRepository contactTypeRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.contactTypeRepository = contactTypeRepository;
        this.userRepository = userRepository;
    }
    
    

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Contact>> createContact(ReqCreateContactDto reqCreateContactDto, UUID userId) {
        
        Contact newContact = new Contact();
        LocalDateTime now = LocalDateTime.now();

        return contactTypeRepository.findByIdAndUserId(reqCreateContactDto.getContactType(), userId)
            .chain(contactTypeOpt -> {
                if (contactTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Contact type not found")));
                }
                newContact.setBusinessName(reqCreateContactDto.getBusinessName().trim());
                newContact.setInternalName(reqCreateContactDto.getInternalName().trim());
                newContact.setDetail(reqCreateContactDto.getDetail().trim());
                newContact.setNote(reqCreateContactDto.getNote());
                newContact.setContactType(contactTypeOpt.get());
                newContact.setAddress(reqCreateContactDto.getAddress());
                newContact.setPhone(reqCreateContactDto.getPhone());
                newContact.setMobilePhone(reqCreateContactDto.getMobilePhone());
                newContact.setLine(reqCreateContactDto.getLine());
                newContact.setEmail(reqCreateContactDto.getEmail());
                newContact.setCreatedAt(now);
                newContact.setUpdatedAt(now);

                return userRepository.findByUserId(userId)
                    .chain(userOpt -> {
                        if (userOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                        }
                        newContact.setCreatedBy(userOpt.get());
                        return contactRepository.persist(newContact)
                                .replaceWith(Either.right(newContact));
                    });
            }); 

    } // end createContact

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Contact>> updateContact(ReqUpdateContactDto reqUpdateContactDto, UUID contactId,
            UUID userId) {
        
        LocalDateTime now = LocalDateTime.now();
        // check contact type in dto is exist
        // check user id
        // get contact and check is it belong to use
        // update contact and return
        return contactTypeRepository.findByIdAndUserId(reqUpdateContactDto.getContactType(), userId)
            .chain(contactTypeOps -> {
                if (contactTypeOps.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Contact type not found")));
                }
                return userRepository.findByUserId(userId)
                    .chain(userOpt -> {
                        if (userOpt.isEmpty()){
                            return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                        }
                        return contactRepository.findByIdAndUserId(contactId, userId)
                        .chain(contactOpt -> {
                            if (contactOpt.isEmpty()) {
                                return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Contact not found")));
                            }
                            Contact existingContact = contactOpt.get();
                            existingContact.setBusinessName(reqUpdateContactDto.getBusinessName().trim());
                            existingContact.setInternalName(reqUpdateContactDto.getInternalName().trim());
                            existingContact.setDetail(reqUpdateContactDto.getDetail().trim());
                            existingContact.setNote(reqUpdateContactDto.getNote());
                            existingContact.setContactType(contactTypeOps.get());
                            existingContact.setAddress(reqUpdateContactDto.getAddress());
                            existingContact.setPhone(reqUpdateContactDto.getPhone());
                            existingContact.setMobilePhone(reqUpdateContactDto.getMobilePhone());
                            existingContact.setLine(reqUpdateContactDto.getLine());
                            existingContact.setEmail(reqUpdateContactDto.getEmail());
                            existingContact.setUpdatedAt(now);

                            return contactRepository.updateContact(existingContact, userId)
                                .chain(updatedContactResult -> updatedContactResult.fold(
                                    error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to update contact: " + error.message()))),
                                    updatedContact -> contactRepository.findByIdAndUserId(updatedContact.getId(), userId)
                                        .map(finalContactOpt -> finalContactOpt.isPresent()
                                            ? Either.right(finalContactOpt.get())
                                            : Either.left(new ServiceError.NotFound("Updated contact not found after update operation"))
                                        )
                                ));
                        });
                    });
            });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> deleteContact(UUID contactId, UUID userId) {
        
        return contactRepository.findByIdAndUserId(contactId, userId)
            .chain(contactOpt -> {
                if (contactOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Contact not found")));
                }
                return contactRepository.deleteContact(contactId, userId)
                    .chain(deleteResult -> deleteResult.fold(
                        deleteError -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to delete contact: " + deleteError.message()))),
                        success -> Uni.createFrom().item(Either.right(success))
                    ));
            });

    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, Contact>> getContactById(UUID contactId, UUID userId) {
        
        return contactRepository.findByIdAndUserId(contactId, userId)
            .chain(contactOpt -> {
                if (contactOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Contact not found")));
                }
                return Uni.createFrom().item(Either.right(contactOpt.get()));
            });

    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<Contact>>> getAllContacts(UUID userId) {
        
        return contactRepository.findAllByUserId(userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to retrieve contacts: " + error.message()))),
                contacts -> Uni.createFrom().item(Either.right(contacts))
            ));
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<Contact>>> getAllContactsByContactType(UUID contactTypeId, UUID userId) {
        
        return contactTypeRepository.findByIdAndUserId(contactTypeId, userId)
            .chain(contactTypeOpt -> {
                if (contactTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Contact type not found")));
                }
                return contactRepository.findByContactTypeAndUserId(contactTypeOpt.get(), userId)
                    .chain(result -> result.fold(
                        error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to retrieve contacts by contact type: " + error.message()))), 
                        success -> Uni.createFrom().item(Either.right(success))
                    )
                );
            });

    }
    
}
