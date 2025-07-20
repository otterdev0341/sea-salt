package com.otterdev.application.usecase.implUsecase.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.application.usecase.internal.base.InternalPropertyUsecase;
import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.Property;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.property.ReqCreatePropertyDto;
import com.otterdev.domain.valueObject.dto.property.ReqUpdatePropertyDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.base.InternalPropertyService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class PropertyUsecaseImpl implements InternalPropertyUsecase {

    private final InternalPropertyService propertyService;



    @Inject
    public PropertyUsecaseImpl(InternalPropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Override
    public Uni<Either<UsecaseError, Property>> createProperty(ReqCreatePropertyDto reqCreatePropertyDto, UUID userId) {
        
        return propertyService.createProperty(reqCreatePropertyDto, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to create property cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(result.getRight()))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, Property>> updateProperty(ReqUpdatePropertyDto reqUpdatePropertyDto,
            UUID propertyId, UUID userId) {
    
        return propertyService.updateProperty(reqUpdatePropertyDto, propertyId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to update property cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(result.getRight()))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, Property>> getPropertyById(UUID propertyId, UUID userId) {
        
        return propertyService.getPropertyById(propertyId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to get property by id cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(result.getRight()))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteProperty(UUID propertyId, UUID userId) {
        
        return propertyService.deleteProperty(propertyId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to delete property cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, List<Property>>> getAllProperties(UUID userId) {
        
        return propertyService.getAllProperties(userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve all properties cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, List<Property>>> getPropertiesByType(UUID propertyTypeId, UUID userId) {
        
        return propertyService.getPropertiesByType(propertyTypeId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve properties by type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, List<Property>>> getPropertiesByStatus(UUID statusId, UUID userId) {
        
        return propertyService.getPropertiesByStatus(statusId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve properties by status cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, List<Property>>> getPropertiesBySold(Boolean isSold, UUID userId) {
        
        return propertyService.getPropertiesBySold(isSold, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve properties by sold status cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, List<FileDetail>>> getAllImagesRelatedById(UUID targetId, UUID userId) {
        
        return propertyService.getAllImagesRelatedById(targetId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve images related by id cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, List<FileDetail>>> getAllPdfRelatedById(UUID targetId, UUID userId) {
    
        return propertyService.getAllPdfRelatedById(targetId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve PDFs related by id cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, List<FileDetail>>> getAllOtherFileRelatedById(UUID targetId, UUID userId) {
        
        return propertyService.getAllOtherFileRelatedById(targetId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve other files related by id cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, List<FileDetail>>> getAllFilesRelatedById(UUID targetId, UUID userId) {
        
        return propertyService.getAllFilesRelatedById(targetId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve all files related by id cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> attachFileToTarget(UUID targetId, RequestAttachFile requestAttachFile,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'attachFileToTarget'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> removeFileFromTarget(UUID taretId, UUID fileId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFileFromTarget'");
    }


    
    
}
