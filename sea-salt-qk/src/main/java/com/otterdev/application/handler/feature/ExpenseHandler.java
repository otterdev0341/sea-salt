package com.otterdev.application.handler.feature;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;
import com.otterdev.application.usecase.internal.base.InternalExpenseUsecase;
import com.otterdev.domain.valueObject.dto.expense.ReqCreateExpenseDto;
import com.otterdev.domain.valueObject.dto.expense.ReqUpdateExpenseDto;
import com.otterdev.domain.valueObject.helper.response.ErrorResponse;
import com.otterdev.domain.valueObject.helper.response.SuccessResponse;
import com.otterdev.infrastructure.service.config.JwtService;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/expenses")
@SecuritySchemes(value = {
    @SecurityScheme(
        securitySchemeName = "jwt",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
    )
})
@SecurityRequirement(name = "jwt")
@ApplicationScoped
public class ExpenseHandler {
    
    private final InternalExpenseUsecase internalExpenseUsecase;
    private final JwtService jwtService;

    @Inject
    public ExpenseHandler(InternalExpenseUsecase internalExpenseUsecase, JwtService jwtService) {
        this.internalExpenseUsecase = internalExpenseUsecase;
        this.jwtService = jwtService;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createExpense(@Valid ReqCreateExpenseDto reqCreateExpenseDto) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();

        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized", 
                "User not authenticated",
                Response.Status.UNAUTHORIZED
            );
            return Uni.createFrom().item(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }

        return internalExpenseUsecase.createExpense(reqCreateExpenseDto, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to create expense"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                contact -> Response
                    .status(Response.Status.CREATED)
                    .entity(new SuccessResponse("Expense created successfully", contact))
                    .build()
            ));

    }// end create



    @PUT
    @Path("/{expenseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateExpense(@PathParam("expenseId") UUID expenseId, @Valid ReqUpdateExpenseDto reqUpdateExpenseDto) {
        
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();

        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized", 
                "User not authenticated",
                Response.Status.UNAUTHORIZED
            );
            return Uni.createFrom().item(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }

        return internalExpenseUsecase.updateExpense(expenseId, reqUpdateExpenseDto, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to update expense"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                expense -> Response
                    .ok(new SuccessResponse("Expense updated successfully", expense))
                    .build()
            ));

    } // end updateExpense


    @DELETE
    @Path("/{expenseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteExpense(@PathParam("expenseId") UUID expenseId) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();

        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized", 
                "User not authenticated",
                Response.Status.UNAUTHORIZED
            );
            return Uni.createFrom().item(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }

        return internalExpenseUsecase.deleteExpense(expenseId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to delete expense"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .ok(new SuccessResponse("Expense deleted successfully", null))
                    .build()
            ));
    } // end deleteExpense


    @GET
    @Path("/{expenseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getExpenseById(@PathParam("expenseId") UUID expenseId){
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();

        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized", 
                "User not authenticated",
                Response.Status.UNAUTHORIZED
            );
            return Uni.createFrom().item(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }

        return internalExpenseUsecase.getExpense(expenseId, userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve expense"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                expense -> Response
                    .ok(new SuccessResponse("Expense retrieved successfully", expense))
                    .build()
            ));
    } // end getExpenseById


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllExpense() {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();

        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized", 
                "User not authenticated",
                Response.Status.UNAUTHORIZED
            );
            return Uni.createFrom().item(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }

        return internalExpenseUsecase.getAllExpenses(userIdOpt.get())
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve expenses"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                expenses -> Response
                    .ok(new SuccessResponse("Expenses retrieved successfully", expenses))
                    .build()
            ));
    } // end getAllExpense


    @GET
    @Path("/expense-type/{expenseTypeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllExpensesByExpenseType(@PathParam("expenseTypeId") UUID expenseTypeId) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();

        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized", 
                "User not authenticated",
                Response.Status.UNAUTHORIZED
            );
            return Uni.createFrom().item(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }

        return internalExpenseUsecase.getAllExpenseByExpenseType(userIdOpt.get(), expenseTypeId)
            .onItem().transform(result -> result.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve expenses by expense type"
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                expenses -> Response
                    .ok(new SuccessResponse("Expenses by expense type retrieved successfully", expenses))
                    .build()
            ));
    } // end getAllExpensesByExpenseType


}// end lass
