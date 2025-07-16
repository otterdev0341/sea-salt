package com.otterdev.application.usecase.internal.ops;

import java.util.List;
import java.util.UUID;
import com.otterdev.domain.entity.PaymentTransaction;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.payment.create.ReqCreatePaymentDto;
import com.otterdev.domain.valueObject.dto.payment.update.ReqUpdatePaymentDto;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalPaymentUsecase {
    
    // common
    @WithTransaction
    Uni<Either<UsecaseError, PaymentTransaction>> createPayment(ReqCreatePaymentDto reqCreatePaymentDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, PaymentTransaction>> updatePayment(ReqUpdatePaymentDto reqUpdatePaymentDto, UUID paymentId, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deletePayment(UUID paymentId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, PaymentTransaction>> getPaymentById(UUID paymentId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, List<PaymentTransaction>>> getAllPayments(UUID userId);

    // query/fillter
    @WithSession
    Uni<Either<UsecaseError, List<PaymentTransaction>>> getAllPaymentsByPropertyId(UUID propertyId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, List<PaymentTransaction>>> getAllPaymentsByContactId(UUID contactId, UUID userId);


    // relation
    // -- implement file detail interface
    
    // specific case
    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> addFileToPaymentById(UUID paymentId, RequestAttachFile file, UUID userId);
    
    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deleteFileFromPaymentById(UUID paymentId, UUID fileId, UUID userId);

}
