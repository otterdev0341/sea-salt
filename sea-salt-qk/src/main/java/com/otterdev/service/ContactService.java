package com.otterdev.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.otterdev.dto.entity.contact.ReqCreateUpdateContactDto;
import com.otterdev.entity.table.Contact;
import com.otterdev.entity.table.ContactType;
import com.otterdev.entity.table.User;
import com.otterdev.error.service.ResourceNotFoundError;
import com.otterdev.error.service.ServiceError;
import com.otterdev.error.service.ValidationError;
import com.otterdev.error.service.UnexpectedError;
import com.otterdev.repository.table.ContactRepository;
import com.otterdev.repository.table.ContactTypeRepository;
import com.otterdev.repository.table.UserRepository;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ContactService {
    
    @Inject
    UserRepository userRepository;

    @Inject
    ContactRepository contactRepository;

    @Inject
    ContactTypeRepository contactTypeRepository;


    @WithTransaction
    public Uni<Either<ServiceError, Contact>> newContact(ReqCreateUpdateContactDto contactDto, UUID userId) {
        // Validate input
        if (contactDto == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Contact data cannot be null")));
        }
        
        if (contactDto.getContactTypeId() == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Contact type ID is required")));
        }
        
        // First, fetch the User entity to ensure it exists
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Contact>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }

                User user = userOpt.get();
                
                // Then, fetch the ContactType entity to ensure it exists and user has access
                return contactTypeRepository.findById(contactDto.getContactTypeId())
                    .chain(contactTypeOpt -> {
                        if (contactTypeOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.<ServiceError, Contact>left(
                                new ResourceNotFoundError("Contact type not found with ID: " + contactDto.getContactTypeId())
                            ));
                        }
                        
                        ContactType contactType = contactTypeOpt.get();
                        
                        // Check if user has permission to use this contact type (only creator can use)
                        if (!contactType.getCreatedBy().getId().equals(userId)) {
                            return Uni.createFrom().item(Either.<ServiceError, Contact>left(
                                new ValidationError("You don't have permission to use this contact type")
                            ));
                        }
                        
                        LocalDateTime now = LocalDateTime.now();

                        // Create new Contact entity with proper relationships and timestamps
                        Contact newContact = new Contact();
                        newContact.setBusinessName(contactDto.getBusinessName());
                        newContact.setInternalName(contactDto.getInternalName());
                        newContact.setDetail(contactDto.getDetail());
                        newContact.setNote(contactDto.getNote());
                        newContact.setContactType(contactType);
                        newContact.setCreatedBy(user);
                        newContact.setCreatedAt(now);
                        newContact.setUpdatedAt(now);

                        // Save to repository
                        return contactRepository.persist(newContact)
                            .map(savedContact -> Either.<ServiceError, Contact>right(savedContact))
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, Contact>left(
                                    new UnexpectedError("Failed to create contact: " + throwable.getMessage(), throwable)
                                )
                            );
                    });
            });
    }// end newContact

    @WithTransaction
    public Uni<Either<ServiceError, Contact>> editContact(UUID contactId, ReqCreateUpdateContactDto contactDto, UUID userId) {
        // Validate input
        if (contactDto == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Contact data cannot be null")));
        }
        
        if (contactDto.getContactTypeId() == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Contact type ID is required")));
        }

        return contactRepository.findById(contactId)
            .chain(contactOpt -> {
                if (contactOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Contact>left(
                        new ResourceNotFoundError("Contact not found with ID: " + contactId)
                    ));
                }
                
                Contact contact = contactOpt.get();
                
                // Check if user has permission to update (only creator can update)
                if (!contact.getCreatedBy().getId().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, Contact>left(
                        new ValidationError("You don't have permission to update this contact")
                    ));
                }
                
                // Validate ContactType if it's being changed
                return contactTypeRepository.findById(contactDto.getContactTypeId())
                    .chain(contactTypeOpt -> {
                        if (contactTypeOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.<ServiceError, Contact>left(
                                new ResourceNotFoundError("Contact type not found with ID: " + contactDto.getContactTypeId())
                            ));
                        }
                        
                        ContactType contactType = contactTypeOpt.get();
                        
                        // Check if user has permission to use this contact type
                        if (!contactType.getCreatedBy().getId().equals(userId)) {
                            return Uni.createFrom().item(Either.<ServiceError, Contact>left(
                                new ValidationError("You don't have permission to use this contact type")
                            ));
                        }
                        
                        // Update fields
                        contact.setBusinessName(contactDto.getBusinessName());
                        contact.setInternalName(contactDto.getInternalName());
                        contact.setDetail(contactDto.getDetail());
                        contact.setNote(contactDto.getNote());
                        contact.setContactType(contactType);
                        contact.setUpdatedAt(LocalDateTime.now());
                        
                        return contactRepository.persist(contact)
                            .map(updatedContact -> Either.<ServiceError, Contact>right(updatedContact))
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, Contact>left(
                                    new UnexpectedError("Failed to update contact: " + throwable.getMessage(), throwable)
                                )
                            );
                    });
            });
    } // end editContact


    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> removeContact(UUID contactId, UUID userId) {
        return contactRepository.findById(contactId)
            .chain(contactOpt -> {
                if (contactOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ResourceNotFoundError("Contact not found with ID: " + contactId)
                    ));
                }
                
                Contact contact = contactOpt.get();
                
                // Check if user has permission to delete (only creator can delete)
                if (!contact.getCreatedBy().getId().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ValidationError("You don't have permission to delete this contact")
                    ));
                }
                
                return contactRepository.delete(contact)
                    .map(deleted -> Either.<ServiceError, Boolean>right(true))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, Boolean>left(
                            new UnexpectedError("Failed to delete contact: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    } // end removeContact


    @WithSession
    public Uni<Either<ServiceError, Contact>> viewContactById(UUID contactId, UUID userId) {
        return contactRepository.findById(contactId)
            .map(contactOpt -> {
                if (contactOpt.isEmpty()) {
                    return Either.<ServiceError, Contact>left(
                        new ResourceNotFoundError("Contact not found with ID: " + contactId)
                    );
                }
                
                Contact contact = contactOpt.get();
                
                // Check if user has permission to view (only creator can view)
                if (!contact.getCreatedBy().getId().equals(userId)) {
                    return Either.<ServiceError, Contact>left(
                        new ValidationError("You don't have permission to view this contact")
                    );
                }
                
                return Either.<ServiceError, Contact>right(contact);
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, Contact>left(
                    new UnexpectedError("Failed to retrieve contact: " + throwable.getMessage(), throwable)
                )
            );
    }

    @WithSession
    public Uni<Either<ServiceError, List<Contact>>> viewAllUserContacts(UUID userId) {
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, List<Contact>>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }
                
                User user = userOpt.get();
                return contactRepository.find("createdBy", user).list()
                    .map(contacts -> Either.<ServiceError, List<Contact>>right(contacts))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, List<Contact>>left(
                            new UnexpectedError("Failed to retrieve user contacts: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }

    @WithSession
    public Uni<Either<ServiceError, List<Contact>>> viewContactsByContactType(UUID contactTypeId, UUID userId) {
        return contactTypeRepository.findById(contactTypeId)
            .chain(contactTypeOpt -> {
                if (contactTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, List<Contact>>left(
                        new ResourceNotFoundError("Contact type not found with ID: " + contactTypeId)
                    ));
                }
                
                ContactType contactType = contactTypeOpt.get();
                
                // Check if user has permission to view contacts for this contact type
                if (!contactType.getCreatedBy().getId().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, List<Contact>>left(
                        new ValidationError("You don't have permission to view contacts for this contact type")
                    ));
                }
                
                return contactRepository.find("contactType", contactType).list()
                    .map(contacts -> Either.<ServiceError, List<Contact>>right(contacts))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, List<Contact>>left(
                            new UnexpectedError("Failed to retrieve contacts by type: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }
} // end class
