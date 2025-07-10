package com.otterdev.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.otterdev.dto.entity.expenseType.ReqCreateUpdateExpenseTypeDto;
import com.otterdev.entity.table.ExpenseType;
import com.otterdev.error.service.ServiceError;
import com.otterdev.service.ExpenseTypeService;
import com.otterdev.service.JwtService;
import com.spencerwi.either.Either;

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

@ApplicationScoped
@Path("/expense-types")
public class ExpenseTypeController {

    @Inject
    private ExpenseTypeService expenseTypeService;

    @Inject
    private JwtService jwtService;

    // Create a new ExpenseType
    @POST
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createExpenseType(@Valid ReqCreateUpdateExpenseTypeDto expenseTypeDto) {
        
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        
        if (userIdOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        UUID userId = userIdOpt.get();
        Either<ServiceError, ExpenseType> result = expenseTypeService.newExpenseType(expenseTypeDto, userId).await().indefinitely();

        if (result.isLeft()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(result.getLeft()).build();
        }

        return Response.status(Response.Status.CREATED).entity(result.getRight()).build();
    }

    // Edit an existing ExpenseType
    @PUT
    @Path("/{id}")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editExpenseType(@PathParam("id") UUID expenseTypeId, @Valid ReqCreateUpdateExpenseTypeDto expenseTypeDto) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        UUID userId = userIdOpt.get();
        Either<ServiceError, ExpenseType> result = expenseTypeService.editExpenseType(expenseTypeId, expenseTypeDto, userId).await().indefinitely();

        if (result.isLeft()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(result.getLeft()).build();
        }

        return Response.ok(result.getRight()).build();
    }

    // View an ExpenseType by ID
    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    public Response viewExpenseTypeById(@PathParam("id") UUID expenseTypeId) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        UUID userId = userIdOpt.get();
        Either<ServiceError, Optional<ExpenseType>> result = expenseTypeService.viewExpenseTypeById(expenseTypeId, userId).await().indefinitely();

        if (result.isLeft()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(result.getLeft()).build();
        }

        return result.getRight().isPresent()
            ? Response.ok(result.getRight().get()).build()
            : Response.status(Response.Status.NOT_FOUND).build();
    }

    // View all ExpenseTypes for the current user
    @GET
    @Path("/")
    @RolesAllowed("user")
    public Response viewAllExpenseTypes() {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        UUID userId = userIdOpt.get();
        Either<ServiceError, List<ExpenseType>> result = expenseTypeService.viewAllUserExpenseTypes(userId).await().indefinitely();

        if (result.isLeft()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(result.getLeft()).build();
        }

        return Response.ok(result.getRight()).build();
    }

    // Delete an ExpenseType
    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    public Response deleteExpenseType(@PathParam("id") UUID expenseTypeId) {
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        UUID userId = userIdOpt.get();
        Either<ServiceError, Boolean> result = expenseTypeService.deleteExpenseType(expenseTypeId, userId).await().indefinitely();

        if (result.isLeft()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(result.getLeft()).build();
        }

        return Response.noContent().build();
    }
}
