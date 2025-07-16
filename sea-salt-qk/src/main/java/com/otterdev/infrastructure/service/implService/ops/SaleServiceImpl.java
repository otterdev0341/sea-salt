package com.otterdev.infrastructure.service.implService.ops;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.SaleTransaction;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.sale.ReqCreateSaleDto;
import com.otterdev.domain.valueObject.sale.ReqUpdateSaleDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.service.internal.ops.InternalSaleService;
import com.otterdev.infrastructure.service.internal.support.InternalFileRelateService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class SaleServiceImpl implements InternalSaleService, InternalFileRelateService {

    @Override
    public Uni<Either<ServiceError, SaleTransaction>> createSale(ReqCreateSaleDto reqCreateSaleDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSale'");
    }

    @Override
    public Uni<Either<ServiceError, SaleTransaction>> updateSale(ReqUpdateSaleDto reqUpdateSaleDto,
            UUID saleTransactionId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSale'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteSale(UUID saleTransactionId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteSale'");
    }

    @Override
    public Uni<Either<ServiceError, SaleTransaction>> getSaleById(UUID saleTransactionId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSaleById'");
    }

    @Override
    public Uni<Either<ServiceError, List<SaleTransaction>>> getAllSalesByUserId(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllSalesByUserId'");
    }

    @Override
    public Uni<Either<ServiceError, List<SaleTransaction>>> getAllSalesByPropertyId(UUID propertyId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllSalesByPropertyId'");
    }

    @Override
    public Uni<Either<ServiceError, List<SaleTransaction>>> getAllSalesByContactId(UUID contactId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllSalesByContactId'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> addFileToSaleById(UUID saleId, RequestAttachFile file, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFileToSaleById'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteFileFromSaleById(UUID saleId, UUID fileId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFileFromSaleById'");
    }

    @Override
    public Uni<Either<ServiceError, List<FileDetail>>> getAllImagesRelatedById(UUID targetId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllImagesRelatedById'");
    }

    @Override
    public Uni<Either<ServiceError, List<FileDetail>>> getAllPdfRelatedById(UUID targetId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPdfRelatedById'");
    }

    @Override
    public Uni<Either<ServiceError, List<FileDetail>>> getAllOtherFileRelatedById(UUID targetId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllOtherFileRelatedById'");
    }

    @Override
    public Uni<Either<ServiceError, List<FileDetail>>> getAllFilesRelatedById(UUID targetId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllFilesRelatedById'");
    }
    
}
