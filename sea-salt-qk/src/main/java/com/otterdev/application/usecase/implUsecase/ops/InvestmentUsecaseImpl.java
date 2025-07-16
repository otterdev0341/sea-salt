package com.otterdev.application.usecase.implUsecase.ops;

import java.util.List;
import java.util.UUID;

import com.otterdev.application.usecase.internal.ops.InternalInvestmentUsecase;
import com.otterdev.application.usecase.internal.support.InternalFileRelateUsecase;
import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.InvestmentTransaction;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.investment.create.ReqCreateInvestmentDto;
import com.otterdev.domain.valueObject.dto.investment.update.ReqUpdateInvestmentDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.ops.InternalInvestmentService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class InvestmentUsecaseImpl implements InternalInvestmentUsecase, InternalFileRelateUsecase {

    private final InternalInvestmentService investmentService;

    @Inject
    public InvestmentUsecaseImpl(InternalInvestmentService investmentService) {
        this.investmentService = investmentService;
    }


    @Override
    public Uni<Either<UsecaseError, InvestmentTransaction>> createInvestment(
            ReqCreateInvestmentDto reqCreateInvestmentDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createInvestment'");
    }

    @Override
    public Uni<Either<UsecaseError, InvestmentTransaction>> updateInvestment(
            ReqUpdateInvestmentDto reqCreateInvestmentDto, UUID investmentId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInvestment'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteInvestment(UUID investmentId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteInvestment'");
    }

    @Override
    public Uni<Either<UsecaseError, InvestmentTransaction>> getInvestmentById(UUID investmentId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInvestmentById'");
    }

    @Override
    public Uni<Either<UsecaseError, List<InvestmentTransaction>>> getAllInvestments(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllInvestments'");
    }

    @Override
    public Uni<Either<UsecaseError, List<InvestmentTransaction>>> getInvestmentsByProperty(UUID propertyId,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInvestmentsByProperty'");
    }

    @Override
    public Uni<Either<UsecaseError, List<InvestmentTransaction>>> getInvestmentsByContact(UUID contactId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInvestmentsByContact'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> addFileToInvestmentById(UUID investmentId, RequestAttachFile file,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFileToInvestmentById'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteFileFromInvestmentById(UUID investmentId, UUID fileId,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFileFromInvestmentById'");
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
