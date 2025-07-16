package com.otterdev.application.usecase.internal.ops;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.SaleTransaction;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.sale.ReqCreateSaleDto;
import com.otterdev.domain.valueObject.sale.ReqUpdateSaleDto;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalSaleUsecase {
    
    // common
    @WithTransaction
    Uni<Either<UsecaseError, SaleTransaction>> createSale(ReqCreateSaleDto reqCreateSaleDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, SaleTransaction>> updateSale(ReqUpdateSaleDto reqUpdateSaleDto, UUID saleTransactionId, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deleteSale(UUID saleTransactionId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, SaleTransaction>> getSaleById(UUID saleTransactionId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, List<SaleTransaction>>> getAllSalesByUserId(UUID userId);

    // file relation
    // need to implement other interface

    // filter/query 
    @WithSession
    Uni<Either<UsecaseError, List<SaleTransaction>>> getAllSalesByPropertyId(UUID propertyId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, List<SaleTransaction>>> getAllSalesByContactId(UUID contactId, UUID userId);

     // -- specific case
    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> addFileToSaleById(UUID saleId, RequestAttachFile file, UUID userId);
    
    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deleteFileFromSaleById(UUID saleId, UUID fileId, UUID userId);

}
