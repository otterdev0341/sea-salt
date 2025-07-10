package com.otterdev.controller;

import java.util.Optional;
import java.util.UUID;

import com.otterdev.dto.entity.expense.ReqCreateUpdateExpenseDto;
import com.otterdev.dto.helper.response.ErrorResponse;
import com.otterdev.dto.helper.response.SuccessResponse;
import com.otterdev.entity.table.Expense;
import com.otterdev.error.service.ServiceError;
import com.otterdev.service.ExpenseService;
import com.otterdev.service.JwtService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
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
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExpenseController {

    @Inject
    private ExpenseService expenseService;

    @Inject
    private JwtService jwtService;

    // Create a new Expense
    @POST
    @RolesAllowed("user")
    public Uni<Response> createExpense(@Valid ReqCreateUpdateExpenseDto expenseDto) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();

        return expenseService.newExpense(expenseDto, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Expense Creation Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }

                Expense newExpense = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Expense created successfully", newExpense);
                return Response.status(Response.Status.CREATED)
                    .entity(successResponse)
                    .build();
            });
    }

    // Edit an existing Expense
    @PUT
    @Path("/{id}")
    @RolesAllowed("user")
    public Uni<Response> updateExpense(@PathParam("id") UUID expenseId, @Valid ReqCreateUpdateExpenseDto expenseDto) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();

        return expenseService.editExpense(expenseId, expenseDto, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Expense Update Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }

                Expense updatedExpense = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Expense updated successfully", updatedExpense);
                return Response.ok(successResponse).build();
            });
    }

    // Delete an Expense
    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    public Uni<Response> deleteExpense(@PathParam("id") UUID expenseId) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();

        return expenseService.deleteExpense(expenseId, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Expense Deletion Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }

                SuccessResponse successResponse = new SuccessResponse("Expense deleted successfully", null);
                return Response.ok(successResponse).build();
            });
    }

    // View an Expense by ID
    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    public Uni<Response> getExpenseById(@PathParam("id") UUID expenseId) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();

        return expenseService.viewExpenseById(expenseId, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Expense Retrieval Failed", error.message());
                    return Response.status(Response.Status.NOT_FOUND)
                        .entity(errorResponse)
                        .build();
                }

                Expense expense = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Expense retrieved successfully", expense);
                return Response.ok(successResponse).build();
            });
    }

    // View all Expenses for the current user
    @GET
    @RolesAllowed("user")
    public Uni<Response> getUserExpenses() {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();

        return expenseService.viewAllUserExpenses(userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Expenses Retrieval Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }

                SuccessResponse successResponse = new SuccessResponse("Expenses retrieved successfully", result.getRight());
                return Response.ok(successResponse).build();
            });
    }
}
