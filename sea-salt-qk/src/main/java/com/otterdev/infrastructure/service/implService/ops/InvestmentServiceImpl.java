package com.otterdev.infrastructure.service.implService.ops;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.InvestmentTransaction;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.investment.create.ReqCreateInvestmentDto;
import com.otterdev.domain.valueObject.dto.investment.update.ReqUpdateInvestmentDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.service.internal.ops.InternalInvestmentService;
import com.otterdev.infrastructure.service.internal.support.InternalFileRelateService;
import com.spencerwi.either.Either;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
class InvestmentServiceImpl implements InternalInvestmentService, InternalFileRelateService {

    @Override
    public Uni<Either<ServiceError, InvestmentTransaction>> createInvestment(
            ReqCreateInvestmentDto reqCreateInvestmentDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createInvestment'");
    }

    @Override
    public Uni<Either<ServiceError, InvestmentTransaction>> updateInvestment(
            ReqUpdateInvestmentDto reqCreateInvestmentDto, UUID investmentId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInvestment'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteInvestment(UUID investmentId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteInvestment'");
    }

    @Override
    public Uni<Either<ServiceError, InvestmentTransaction>> getInvestmentById(UUID investmentId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInvestmentById'");
    }

    @Override
    public Uni<Either<ServiceError, List<InvestmentTransaction>>> getAllInvestments(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllInvestments'");
    }

    @Override
    public Uni<Either<ServiceError, List<InvestmentTransaction>>> getInvestmentsByProperty(UUID propertyId,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInvestmentsByProperty'");
    }

    @Override
    public Uni<Either<ServiceError, List<InvestmentTransaction>>> getInvestmentsByContact(UUID contactId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInvestmentsByContact'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> addFileToInvestmentById(UUID investmentId, RequestAttachFile file,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFileToInvestmentById'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteFileFromInvestmentById(UUID investmentId, UUID fileId,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFileFromInvestmentById'");
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
