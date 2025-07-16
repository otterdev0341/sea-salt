package com.otterdev.infrastructure.service.internal.ops;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.InvestmentTransaction;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.investment.create.ReqCreateInvestmentDto;
import com.otterdev.domain.valueObject.dto.investment.update.ReqUpdateInvestmentDto;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalInvestmentService {
    
    // common
    @WithTransaction
    Uni<Either<ServiceError, InvestmentTransaction>> createInvestment(ReqCreateInvestmentDto reqCreateInvestmentDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, InvestmentTransaction>> updateInvestment(ReqUpdateInvestmentDto reqCreateInvestmentDto, UUID investmentId, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> deleteInvestment(UUID investmentId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, InvestmentTransaction>> getInvestmentById(UUID investmentId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, List<InvestmentTransaction>>> getAllInvestments(UUID userId);

    // filter/ query
    @WithSession
    Uni<Either<ServiceError, List<InvestmentTransaction>>> getInvestmentsByProperty(UUID propertyId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, List<InvestmentTransaction>>> getInvestmentsByContact(UUID contactId, UUID userId);

    // file relation
    // -- impl interface

    // -- specific case
    @WithTransaction
    Uni<Either<ServiceError, Boolean>> addFileToInvestmentById(UUID investmentId, RequestAttachFile file, UUID userId);
    
    @WithTransaction
    Uni<Either<ServiceError, Boolean>> deleteFileFromInvestmentById(UUID investmentId, UUID fileId, UUID userId);

}
