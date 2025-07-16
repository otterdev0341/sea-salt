package com.otterdev.application.usecase.implUsecase.ops;

import java.util.List;
import java.util.UUID;

import com.otterdev.application.usecase.internal.ops.InternalSaleUsecase;
import com.otterdev.application.usecase.internal.support.InternalFileRelateUsecase;
import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.SaleTransaction;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.sale.ReqCreateSaleDto;
import com.otterdev.domain.valueObject.sale.ReqUpdateSaleDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.ops.InternalSaleService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
class SaleUsecaseImpl implements InternalSaleUsecase, InternalFileRelateUsecase {

    private final InternalSaleService saleService;

    @Inject
    public SaleUsecaseImpl(InternalSaleService saleService) {
        this.saleService = saleService;
    }
    

    @Override
    public Uni<Either<UsecaseError, SaleTransaction>> createSale(ReqCreateSaleDto reqCreateSaleDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSale'");
    }

    @Override
    public Uni<Either<UsecaseError, SaleTransaction>> updateSale(ReqUpdateSaleDto reqUpdateSaleDto,
            UUID saleTransactionId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSale'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteSale(UUID saleTransactionId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteSale'");
    }

    @Override
    public Uni<Either<UsecaseError, SaleTransaction>> getSaleById(UUID saleTransactionId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSaleById'");
    }

    @Override
    public Uni<Either<UsecaseError, List<SaleTransaction>>> getAllSalesByUserId(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllSalesByUserId'");
    }

    @Override
    public Uni<Either<UsecaseError, List<SaleTransaction>>> getAllSalesByPropertyId(UUID propertyId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllSalesByPropertyId'");
    }

    @Override
    public Uni<Either<UsecaseError, List<SaleTransaction>>> getAllSalesByContactId(UUID contactId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllSalesByContactId'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> addFileToSaleById(UUID saleId, RequestAttachFile file, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFileToSaleById'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteFileFromSaleById(UUID saleId, UUID fileId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFileFromSaleById'");
    }


    @Override
    public Uni<Either<UsecaseError, List<FileDetail>>> getAllImagesRelatedById(UUID targetId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllImagesRelatedById'");
    }


    @Override
    public Uni<Either<UsecaseError, List<FileDetail>>> getAllPdfRelatedById(UUID targetId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPdfRelatedById'");
    }


    @Override
    public Uni<Either<UsecaseError, List<FileDetail>>> getAllOtherFileRelatedById(UUID targetId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllOtherFileRelatedById'");
    }


    @Override
    public Uni<Either<UsecaseError, List<FileDetail>>> getAllFilesRelatedById(UUID targetId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllFilesRelatedById'");
    }
    
}
