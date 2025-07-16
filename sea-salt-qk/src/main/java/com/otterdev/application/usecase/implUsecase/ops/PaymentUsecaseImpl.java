package com.otterdev.application.usecase.implUsecase.ops;

import java.util.List;
import java.util.UUID;

import com.otterdev.application.usecase.internal.ops.InternalPaymentUsecase;
import com.otterdev.application.usecase.internal.support.InternalFileRelateUsecase;
import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.PaymentTransaction;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.payment.create.ReqCreatePaymentDto;
import com.otterdev.domain.valueObject.dto.payment.update.ReqUpdatePaymentDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.ops.InternalPaymentService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class PaymentUsecaseImpl implements InternalPaymentUsecase, InternalFileRelateUsecase {

    private final InternalPaymentService paymentService;

    @Inject
    public PaymentUsecaseImpl(InternalPaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @Override
    public Uni<Either<UsecaseError, PaymentTransaction>> createPayment(ReqCreatePaymentDto reqCreatePaymentDto,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPayment'");
    }

    @Override
    public Uni<Either<UsecaseError, PaymentTransaction>> updatePayment(ReqUpdatePaymentDto reqUpdatePaymentDto,
            UUID paymentId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePayment'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deletePayment(UUID paymentId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePayment'");
    }

    @Override
    public Uni<Either<UsecaseError, PaymentTransaction>> getPaymentById(UUID paymentId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentById'");
    }

    @Override
    public Uni<Either<UsecaseError, List<PaymentTransaction>>> getAllPayments(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPayments'");
    }

    @Override
    public Uni<Either<UsecaseError, List<PaymentTransaction>>> getAllPaymentsByPropertyId(UUID propertyId,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPaymentsByPropertyId'");
    }

    @Override
    public Uni<Either<UsecaseError, List<PaymentTransaction>>> getAllPaymentsByContactId(UUID contactId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPaymentsByContactId'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> addFileToPaymentById(UUID paymentId, RequestAttachFile file,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFileToPaymentById'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteFileFromPaymentById(UUID paymentId, UUID fileId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFileFromPaymentById'");
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
