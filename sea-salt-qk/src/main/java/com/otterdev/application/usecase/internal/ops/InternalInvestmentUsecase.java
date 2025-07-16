package com.otterdev.application.usecase.internal.ops;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.InvestmentTransaction;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.investment.create.ReqCreateInvestmentDto;
import com.otterdev.domain.valueObject.dto.investment.update.ReqUpdateInvestmentDto;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalInvestmentUsecase {
    
    // common
    @WithTransaction
    Uni<Either<UsecaseError, InvestmentTransaction>> createInvestment(ReqCreateInvestmentDto reqCreateInvestmentDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, InvestmentTransaction>> updateInvestment(ReqUpdateInvestmentDto reqCreateInvestmentDto, UUID investmentId, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deleteInvestment(UUID investmentId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, InvestmentTransaction>> getInvestmentById(UUID investmentId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, List<InvestmentTransaction>>> getAllInvestments(UUID userId);

    // filter/ query
    @WithSession
    Uni<Either<UsecaseError, List<InvestmentTransaction>>> getInvestmentsByProperty(UUID propertyId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, List<InvestmentTransaction>>> getInvestmentsByContact(UUID contactId, UUID userId);

    // file relation
    // -- impl interface

    // -- specific case
    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> addFileToInvestmentById(UUID investmentId, RequestAttachFile file, UUID userId);
    
    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deleteFileFromInvestmentById(UUID investmentId, UUID fileId, UUID userId);

}
