package com.otterdev.infrastructure.service.implService.base;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.FileType;
import com.otterdev.domain.entity.Property;
import com.otterdev.domain.entity.relation.PropertyFileDetail;
import com.otterdev.domain.valueObject.cloudflare.ResFileR2Dto;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.property.ReqCreatePropertyDto;
import com.otterdev.domain.valueObject.dto.property.ReqUpdatePropertyDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.CloudFlareR2RepositoryImpl;
import com.otterdev.infrastructure.repository.ContactRepository;
import com.otterdev.infrastructure.repository.FileDetailRepository;
import com.otterdev.infrastructure.repository.FileTypeRepository;
import com.otterdev.infrastructure.repository.PropertyFileDetailRepository;
import com.otterdev.infrastructure.repository.PropertyRepository;
import com.otterdev.infrastructure.repository.PropertyStatusRepository;
import com.otterdev.infrastructure.repository.UserRepository;
import com.otterdev.infrastructure.repository.support.InternalFileRelateRepository;
import com.otterdev.infrastructure.service.internal.base.InternalPropertyService;
import com.spencerwi.either.Either;


import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("propertyService")  // Add this qualifier
class PropertyServiceImpl implements InternalPropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final CloudFlareR2RepositoryImpl cloudFlareR2Repository;
    private final PropertyStatusRepository propertyStatusRepository;
    private final ContactRepository contactRepository;
    private final PropertyFileDetailRepository propertyFileDetailRepository;
    private final FileDetailRepository fileDetailRepository;
    private final FileTypeRepository fileTypeRepository;
    private final InternalFileRelateRepository propertyFileDetailRelatedRepository;


    @Inject
    public PropertyServiceImpl(
        PropertyRepository propertyRepository, 
        UserRepository userRepository, 
        CloudFlareR2RepositoryImpl cloudFlareR2Repository, 
        PropertyStatusRepository propertyStatusRepository, 
        ContactRepository contactRepository, 
        PropertyFileDetailRepository propertyFileDetailRepository, 
        FileDetailRepository fileDetailRepository, 
        FileTypeRepository fileTypeRepository, 
        @Named("propertyFileDetailRepository") InternalFileRelateRepository propertyFileDetailRelatedRepository
        ) 
    {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.cloudFlareR2Repository = cloudFlareR2Repository;
        this.propertyStatusRepository = propertyStatusRepository;
        this.contactRepository = contactRepository;
        this.propertyFileDetailRepository = propertyFileDetailRepository;
        this.fileDetailRepository = fileDetailRepository;
        this.fileTypeRepository = fileTypeRepository;
        this.propertyFileDetailRelatedRepository = propertyFileDetailRelatedRepository;
    }


    
    
    
    
  
    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Property>> createProperty(ReqCreatePropertyDto reqCreatePropertyDto, UUID userId) {

        // create property
        return helperCreateNewProperty(reqCreatePropertyDto, userId)
            .chain(propertyResult -> propertyResult.fold(
                // If property creation failed
                error -> Uni.createFrom().item(Either.left(
                    new ServiceError.OperationFailed("Failed to create property: " + error.message())
                )),

                // If property created successfully
                property -> {
                    if (reqCreatePropertyDto.getFiles() == null || reqCreatePropertyDto.getFiles().isEmpty()) {
                        return Uni.createFrom().item(Either.right(property));
                    }

                    // Process files sequentially
                    return Multi.createFrom().iterable(reqCreatePropertyDto.getFiles())
                    .onItem().transformToUniAndConcatenate(file ->
                        helpUploadFile(file, property, userId)
                            .chain(fileDetailResult -> fileDetailResult.fold(
                                fileError -> Uni.createFrom().failure(
                                    new RuntimeException("Failed to upload file: " + fileError.message())
                                ),
                                fileDetail -> createRelationPropertyFileDetail(fileDetail, property, userId)
                            ))
                    )
                    .collect().asList()
                    .replaceWith(Either.right(property));
                }
            ));
    } // end createProperty


    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Property>> updateProperty(ReqUpdatePropertyDto reqUpdatePropertyDto,
            UUID propertyId, UUID userId) {
        // get user object
        // is new property name is not exist
        // is property status exist
        // is contact exist
        // is property exist
        // persist update
        LocalDateTime now = LocalDateTime.now();
        return userRepository.findByUserId(userId)
        .chain(userOpt -> {
            if (userOpt.isEmpty()) {
                return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
            }
            return propertyRepository.isExistByNameAndUserId(reqUpdatePropertyDto.getName(), userOpt.get().getId())
            .chain(isNameExist -> {
                if (isNameExist) {
                    return Uni.createFrom().item(Either.left(new ServiceError.BusinessRuleFailed("Property name already exists")));
                }
                return propertyStatusRepository.findByIdAndUserId(reqUpdatePropertyDto.getStatus(), userOpt.get().getId())
                .chain(propertyStatusOpt -> {
                    if (propertyStatusOpt.isEmpty()) {
                        return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Property status not found")));
                    }
                    return contactRepository.findByIdAndUserId(reqUpdatePropertyDto.getOwnerBy(), userOpt.get().getId())
                    .chain(contactOpt -> {
                        if (contactOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Contact not found")));
                        }
                        return propertyRepository.findByIdAndUserId(propertyId, userOpt.get().getId())
                        .chain(propertyOpt -> {
                            if (propertyOpt.isEmpty()) {
                                return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Property not found")));
                            }
                            Property existingProperty = propertyOpt.get();
                            existingProperty.setName(reqUpdatePropertyDto.getName().trim());
                            existingProperty.setDescription(reqUpdatePropertyDto.getDescription());
                            existingProperty.setSpecific(reqUpdatePropertyDto.getSpecific());
                            existingProperty.setHilight(reqUpdatePropertyDto.getHilight());
                            existingProperty.setArea(reqUpdatePropertyDto.getArea());
                            existingProperty.setPrice(new BigDecimal(reqUpdatePropertyDto.getPrice()));
                            existingProperty.setFsp(new BigDecimal(reqUpdatePropertyDto.getFsp()));
                            existingProperty.setStatus(propertyStatusOpt.get());
                            existingProperty.setOwnerBy(contactOpt.get());
                            existingProperty.setMapUrl(reqUpdatePropertyDto.getMapUrl());
                            existingProperty.setLat(reqUpdatePropertyDto.getLat());
                            existingProperty.setLng(reqUpdatePropertyDto.getLng());
                            existingProperty.setUpdatedAt(now);

                            return propertyRepository.updateProperty(existingProperty, userId)
                            .chain(updatePropertyResult -> updatePropertyResult.fold(
                                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to update property: " + error.message()))), 
                                updatedProperty -> propertyRepository.findByIdAndUserId(updatedProperty.getId(), userId)
                                .map(finalPropertyOpt -> finalPropertyOpt.isPresent()
                                ? Either.right(finalPropertyOpt.get())
                                : Either.left(new ServiceError.NotFound("Updated property not found after update operation"))
                                )
                                )//end map
                            ); // enc chain
                        }); // end property check
                    }); // end contact check
                }); // end property status check
            }); // end name exist check
        }); // end user
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, Property>> getPropertyById(UUID propertyId, UUID userId) {
        
        return propertyRepository.findByIdAndUserId(propertyId, userId)
                .chain(result -> {
                    if (result.isEmpty()) {
                        return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Property not found")));
                    }
                    return Uni.createFrom().item(Either.right(result.get()));
                });

    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> deleteProperty(UUID propertyId, UUID userId) {
        
        return propertyRepository.deleteProperty(propertyId, userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to delete property: " + error.message()))),
                success -> Uni.createFrom().item(Either.right(success))
            ));

    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<Property>>> getAllProperties(UUID userId) {
        
        return propertyRepository.findAllByUserId(userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to retrieve properties: " + error.message()))),
                properties -> Uni.createFrom().item(Either.right(properties))
            ));

    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<Property>>> getPropertiesByType(UUID propertyTypeId, UUID userId) {
        
        return propertyRepository.findAllPropertiesByStatusAndUserId(propertyTypeId, userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to retrieve properties by type: " + error.message()))),
                properties -> Uni.createFrom().item(Either.right(properties))
            ));
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<Property>>> getPropertiesByStatus(UUID statusId, UUID userId) {
        
        return propertyRepository.findAllPropertiesByStatusAndUserId(statusId, userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to retrieve properties by status: " + error.message()))),
                properties -> Uni.createFrom().item(Either.right(properties))
            ));

    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<Property>>> getPropertiesBySold(Boolean isSold, UUID userId) {
        
        return propertyRepository.getAllPropertiesBySold(isSold, userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to retrieve properties by sold status: " + error.message()))),
                properties -> Uni.createFrom().item(Either.right(properties))
            ));
        
    }
    

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<FileDetail>>> getAllImagesRelatedById(UUID targetId, UUID userId) {
        return userRepository.findByUserId(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                }
                // getFileTypeByExtention อาจคืน Uni<Either<ServiceError, FileType>>
                return fileTypeRepository.getFileTypeByExtention("image")
                    .chain(fileTypeResult -> {
                        if (fileTypeResult.isLeft()) {
                            return Uni.createFrom().item(Either.left(new ServiceError.NotFound("File type not found for content type: image")));
                        }
                        FileType fileType = fileTypeResult.getRight();
                        return propertyFileDetailRelatedRepository.getAllFileRelatedById(targetId, userOpt.get().getId(), fileType)
                            .chain(fileDetailsResult -> fileDetailsResult.fold(
                                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to retrieve image files: " + error.message()))),
                                fileDetails -> Uni.createFrom().item(Either.right(fileDetails))
                            ));
                    });
            });
    }






    @Override
    @WithSession
    public Uni<Either<ServiceError, List<FileDetail>>> getAllPdfRelatedById(UUID targetId, UUID userId) {
        return userRepository.findByUserId(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                }
                // getFileTypeByExtention อาจคืน Uni<Either<ServiceError, FileType>>
                return fileTypeRepository.getFileTypeByExtention("pdf")
                    .chain(fileTypeResult -> {
                        if (fileTypeResult.isLeft()) {
                            return Uni.createFrom().item(Either.left(new ServiceError.NotFound("File type not found for content type: image")));
                        }
                        FileType fileType = fileTypeResult.getRight();
                        return propertyFileDetailRelatedRepository.getAllFileRelatedById(targetId, userOpt.get().getId(), fileType)
                            .chain(fileDetailsResult -> fileDetailsResult.fold(
                                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to retrieve image files: " + error.message()))),
                                fileDetails -> Uni.createFrom().item(Either.right(fileDetails))
                            ));
                    });
            });
    }






    @Override
    @WithSession
    public Uni<Either<ServiceError, List<FileDetail>>> getAllOtherFileRelatedById(UUID targetId, UUID userId) {
        return userRepository.findByUserId(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                }
                // getFileTypeByExtention อาจคืน Uni<Either<ServiceError, FileType>>
                return fileTypeRepository.getFileTypeByExtention("other")
                    .chain(fileTypeResult -> {
                        if (fileTypeResult.isLeft()) {
                            return Uni.createFrom().item(Either.left(new ServiceError.NotFound("File type not found for content type: image")));
                        }
                        FileType fileType = fileTypeResult.getRight();
                        return propertyFileDetailRelatedRepository.getAllFileRelatedById(targetId, userOpt.get().getId(), fileType)
                            .chain(fileDetailsResult -> fileDetailsResult.fold(
                                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to retrieve image files: " + error.message()))),
                                fileDetails -> Uni.createFrom().item(Either.right(fileDetails))
                            ));
                    });
            });
    }






    @Override
    @WithSession
    public Uni<Either<ServiceError, List<FileDetail>>> getAllFilesRelatedById(UUID targetId, UUID userId) {
        return userRepository.findByUserId(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                }
                // getFileTypeByExtention อาจคืน Uni<Either<ServiceError, FileType>>
                return propertyFileDetailRelatedRepository.getAllFilesRelatedById(targetId, userOpt.get().getId())
                            .chain(fileDetailsResult -> fileDetailsResult.fold(
                                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to retrieve image files: " + error.message()))),
                                fileDetails -> Uni.createFrom().item(Either.right(fileDetails))
                            ));
            });
    }



    // helper method
    @WithTransaction
    public Uni<Either<ServiceError, Property>> helperCreateNewProperty(ReqCreatePropertyDto reqCreatePropertyDto, UUID userId) {
        
        LocalDateTime now = LocalDateTime.now();
        // check property stattus
        // check user
        // persis
        // return property
        
        return propertyStatusRepository.findByIdAndUserId(reqCreatePropertyDto.getPropertyStatusAsUUID(), userId)
            .chain(propertyStatusOpt -> {
                if (propertyStatusOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Property status not found")));
                }

                return contactRepository.findByIdAndUserId(reqCreatePropertyDto.getOwnerByAsUUID(), userId)
                .chain(contactOpt -> {
                    if (contactOpt.isEmpty()) {
                        return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Contact not found")));
                    }
                    return userRepository.findByUserId(userId)
                    .chain(userOpt -> {
                        if (userOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                        }

                        Property newProperty = new Property();
                        newProperty.setName(reqCreatePropertyDto.getName().trim());
                        newProperty.setDescription(reqCreatePropertyDto.getDescription());
                        newProperty.setSpecific(reqCreatePropertyDto.getSpecific());
                        newProperty.setHilight(reqCreatePropertyDto.getHilight());
                        newProperty.setArea(reqCreatePropertyDto.getArea());
                        newProperty.setPrice(new BigDecimal(reqCreatePropertyDto.getPriceAsDouble()));
                        newProperty.setFsp(new BigDecimal(reqCreatePropertyDto.getFspAsDouble()));
                        newProperty.setStatus(propertyStatusOpt.get());
                        newProperty.setOwnerBy(contactOpt.get());
                        newProperty.setMapUrl(reqCreatePropertyDto.getMapUrl());
                        newProperty.setLat(reqCreatePropertyDto.getLat());
                        newProperty.setLng(reqCreatePropertyDto.getLng());
                        newProperty.setCreatedBy(userOpt.get());
                        newProperty.setCreatedAt(now);
                        newProperty.setUpdatedAt(now);

                        return propertyRepository.persist(newProperty)
                            .replaceWith(Either.right(newProperty));
                    });
                });
                

            });
    } // help create new property

    public Uni<Either<ServiceError, FileDetail>> helpUploadFile(FileUpload file, Property property ,UUID userId) {
        
        // type
        // user
        // upload to cloud flare
        // persist to file detail

        return fileTypeRepository.getFileTypeByExtention(file.contentType())
            .chain(fileType -> {
                if (fileType.isLeft()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("File type not found for content type: " + file.contentType())));
                }
                return userRepository.findByUserId(userId)
                .chain(userOpt -> {
                    if (userOpt.isEmpty()) {
                        return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                    }
                    return cloudFlareR2Repository.uploadFile(file)
                    .chain(uploadedFile -> {
                        if (uploadedFile.isLeft()) {
                            return Uni.createFrom().item(Either.left(new ServiceError.BusinessRuleFailed("Failed to upload file: " + uploadedFile.getLeft().message())));
                        }
                        ResFileR2Dto uploadedFileDto = uploadedFile.getRight();
                        FileDetail newFileDetail = new FileDetail();
                        newFileDetail.setName(uploadedFileDto.getFileName());
                        newFileDetail.setObjectKey(uploadedFileDto.getObjectKey());
                        newFileDetail.setPath(uploadedFileDto.getFileUrl());
                        newFileDetail.setType(fileType.getRight());
                        newFileDetail.setSize(uploadedFileDto.getContentLength());
                        newFileDetail.setCreatedBy(userOpt.get());
                        newFileDetail.setCreatedAt(LocalDateTime.now());
                        newFileDetail.setUpdatedAt(LocalDateTime.now());

                        return fileDetailRepository.persist(newFileDetail)
                            .replaceWith(Either.right(newFileDetail));
                    });
                });
            });
    } // help upload file

    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> createRelationPropertyFileDetail(FileDetail file, Property property, UUID userId) {
        // get user object
        // create now
        // persist relation
        return userRepository.findByUserId(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                }
                PropertyFileDetail propertyFileDetail = new PropertyFileDetail();
                propertyFileDetail.setProperty(property);
                propertyFileDetail.setFile(file);
                propertyFileDetail.setCreatedBy(userOpt.get());
                propertyFileDetail.setCreatedAt(LocalDateTime.now());
                propertyFileDetail.setUpdatedAt(LocalDateTime.now());
                System.out.println("Uploading file: " + file.getName() + " size=" + file.getSize());

                return propertyFileDetailRepository.persist(propertyFileDetail)
                    .replaceWith(Either.right(true));
            });
    } // end







    @Override
    public Uni<Either<ServiceError, Boolean>> attachFileToTarget(UUID targetId, RequestAttachFile requestAttachFile,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'attachFileToTarget'");
    }







    @Override
    public Uni<Either<ServiceError, Boolean>> removeFileFromTarget(UUID taretId, UUID fileId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFileFromTarget'");
    }






}
