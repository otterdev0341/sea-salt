package com.otterdev.infrastructure.service.implService.ops;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.PaymentTransaction;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.payment.create.ReqCreatePaymentDto;
import com.otterdev.domain.valueObject.dto.payment.update.ReqUpdatePaymentDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.service.internal.ops.InternalPaymentService;
import com.otterdev.infrastructure.service.internal.support.InternalFileRelateService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class PaymentServiceImpl implements InternalPaymentService, InternalFileRelateService {

    @Override
    public Uni<Either<ServiceError, PaymentTransaction>> createPayment(ReqCreatePaymentDto reqCreatePaymentDto,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPayment'");
    }

    @Override
    public Uni<Either<ServiceError, PaymentTransaction>> updatePayment(ReqUpdatePaymentDto reqUpdatePaymentDto,
            UUID paymentId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePayment'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deletePayment(UUID paymentId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePayment'");
    }

    @Override
    public Uni<Either<ServiceError, PaymentTransaction>> getPaymentById(UUID paymentId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentById'");
    }

    @Override
    public Uni<Either<ServiceError, List<PaymentTransaction>>> getAllPayments(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPayments'");
    }

    @Override
    public Uni<Either<ServiceError, List<PaymentTransaction>>> getAllPaymentsByPropertyId(UUID propertyId,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPaymentsByPropertyId'");
    }

    @Override
    public Uni<Either<ServiceError, List<PaymentTransaction>>> getAllPaymentsByContactId(UUID contactId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPaymentsByContactId'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> addFileToPaymentById(UUID paymentId, RequestAttachFile file,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFileToPaymentById'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteFileFromPaymentById(UUID paymentId, UUID fileId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFileFromPaymentById'");
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
