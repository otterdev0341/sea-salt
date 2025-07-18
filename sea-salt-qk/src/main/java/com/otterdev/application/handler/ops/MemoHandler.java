package com.otterdev.application.handler.ops;

import java.util.Optional;
import java.util.UUID;
import com.otterdev.application.usecase.internal.ops.InternalMemoUsecase;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.memo.ReqCreateMemoDto;
import com.otterdev.domain.valueObject.dto.memo.ReqUpdateMemoDto;
import com.otterdev.domain.valueObject.helper.response.ErrorResponse;
import com.otterdev.domain.valueObject.helper.response.SuccessResponse;
import com.otterdev.infrastructure.service.config.JwtService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
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

@Path("/ops/memo")
@ApplicationScoped
public class MemoHandler {
    
    private final InternalMemoUsecase internalMemoUsecase;
    private final JwtService jwtService;

    @Inject
    public MemoHandler(InternalMemoUsecase internalMemoUsecase, JwtService jwtService) {
        this.internalMemoUsecase = internalMemoUsecase;
        this.jwtService = jwtService;
    }

    // 1 create memo
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createMemo(@BeanParam @Valid ReqCreateMemoDto reqCreateMemoDto) {
        
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
        
        
        
        return internalMemoUsecase.createMemo(reqCreateMemoDto, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to create memo. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.CREATED)
                    .entity(new SuccessResponse("Memo created successfully", success))
                    .build()
            ));
    } // end class

    
    // 2 update memo
    @PUT
    @Path("/{memoId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateMemo(UUID memoId, @Valid ReqUpdateMemoDto reqUpdateMemoDto) {
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
        
        return internalMemoUsecase.updateMemo(memoId, reqUpdateMemoDto, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to update memo. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Memo updated successfully", success))
                    .build()
            ));
    } // end class


    // 3 delete memo
    @DELETE
    @Path("/{memoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteMemo(UUID memoId) {
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
        
        return internalMemoUsecase.deleteMemo(memoId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to delete memo. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.NO_CONTENT)
                    .build()
            ));
    }

    // 4 get memo by id
    @GET
    @Path("/{memoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getMemoById(UUID memoId) {
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
        
        return internalMemoUsecase.getMemoById(memoId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve memo. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Memo retrieved successfully", success))
                    .build()
            ));
    } // end class

    // 5 get all memos
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllMemos() {
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
        
        return internalMemoUsecase.getAllMemos(userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve memos. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Memos retrieved successfully", success))
                    .build()
            ));
    } // end class

    // 6 get memos by type
    @GET
    @Path("/type/{memoTypeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getMemosByType(UUID memoTypeId) {
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
        
        return internalMemoUsecase.getMemosByType(memoTypeId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve memos by type. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Memos by type retrieved successfully", success))
                    .build()
            ));
    } // end class


    // 7 get memos by property id
    @GET
    @Path("/property/{propertyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getMemosByPropertyId(UUID propertyId) {
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
        
        return internalMemoUsecase.getMemosByPropertyId(propertyId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve memos by property. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Memos by property retrieved successfully", success))
                    .build()
            ));
    } // end class


    // 8 add file to memo
    @POST
    @Path("/{memoId}/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addFileToMemoById(
        @PathParam("memoId") UUID memoId,
        @BeanParam @Valid RequestAttachFile file
    ) {
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
        
        return internalMemoUsecase.addFileToMemoById(memoId, file, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to add file to memo. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("File added to memo successfully", success))
                    .build()
            ));
    } // end class

    // 9 delete file from memo
    @DELETE
    @Path("/{memoId}/file/{fileId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteFileFromMemoById(
        @PathParam("memoId") UUID memoId,
        @PathParam("fileId") UUID fileId
    ) {
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
        
        return internalMemoUsecase.deleteFileFromMemoById(memoId, fileId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to delete file from memo. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.NO_CONTENT)
                    .build()
            ));
    } // end class


    // - getAllImagesRelatedById
    @GET
    @Path("/{memoId}/images")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllImagesRelatedById(@PathParam("memoId") UUID memoId) {
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
        
        return internalMemoUsecase.getAllImagesRelatedById(memoId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve images related to memo. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Images retrieved successfully", success))
                    .build()
            ));
    } // end class

    // - getAllPdfRelatedById
    @GET
    @Path("/{memoId}/pdfs")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllPdfRelatedById(@PathParam("memoId") UUID memoId) {
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
        
        return internalMemoUsecase.getAllPdfRelatedById(memoId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve PDFs related to memo. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("PDFs retrieved successfully", success))
                    .build()
            ));
    } // end class


    // - getAllOtherFileRelatedById
    @GET
    @Path("/{memoId}/other-files")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllOtherFileRelatedById(@PathParam("memoId") UUID memoId) {
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
        
        return internalMemoUsecase.getAllOtherFileRelatedById(memoId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve other files related to memo. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Other files retrieved successfully", success))
                    .build()
            ));
    } // end class

    // - getAllFilesRelatedById
    @GET
    @Path("/{memoId}/files")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllFilesRelatedById(@PathParam("memoId") UUID memoId) {
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
        
        return internalMemoUsecase.getAllFilesRelatedById(memoId, userIdOpt.get())
            .onItem().transform(either -> either.fold(
                error -> {
                    ErrorResponse errorResponse = ErrorResponse.fromUsecaseError(
                        error, 
                        "Failed to retrieve files related to memo. Please try again."
                    );
                    return Response
                        .status(errorResponse.getStatusCode())
                        .entity(errorResponse)
                        .build();
                },
                success -> Response
                    .status(Response.Status.OK)
                    .entity(new SuccessResponse("Files retrieved successfully", success))
                    .build()
            ));
    } // end class

} // end class
