package com.otterdev.infrastructure.service.internal.ops;

import java.util.List;
import java.util.UUID;
import com.otterdev.domain.entity.PaymentTransaction;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.payment.create.ReqCreatePaymentDto;
import com.otterdev.domain.valueObject.dto.payment.update.ReqUpdatePaymentDto;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalPaymentService {
    
    // common
    @WithTransaction
    Uni<Either<ServiceError, PaymentTransaction>> createPayment(ReqCreatePaymentDto reqCreatePaymentDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, PaymentTransaction>> updatePayment(ReqUpdatePaymentDto reqUpdatePaymentDto, UUID paymentId, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> deletePayment(UUID paymentId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, PaymentTransaction>> getPaymentById(UUID paymentId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, List<PaymentTransaction>>> getAllPayments(UUID userId);

    // query/fillter
    @WithSession
    Uni<Either<ServiceError, List<PaymentTransaction>>> getAllPaymentsByPropertyId(UUID propertyId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, List<PaymentTransaction>>> getAllPaymentsByContactId(UUID contactId, UUID userId);


    // relation
    // -- implement file detail interface
    
    // specific case
    @WithTransaction
    Uni<Either<ServiceError, Boolean>> addFileToPaymentById(UUID paymentId, RequestAttachFile file, UUID userId);
    
    @WithTransaction
    Uni<Either<ServiceError, Boolean>> deleteFileFromPaymentById(UUID paymentId, UUID fileId, UUID userId);

}
