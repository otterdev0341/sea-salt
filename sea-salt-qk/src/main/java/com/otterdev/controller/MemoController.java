package com.otterdev.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.otterdev.dto.entity.memo.ReqCreateUpdateMemoDto;
import com.otterdev.dto.helper.response.ErrorResponse;
import com.otterdev.dto.helper.response.SuccessResponse;
import com.otterdev.entity.table.Memo;
import com.otterdev.error.service.ServiceError;
import com.otterdev.service.JwtService;
import com.otterdev.service.MemoFileService;
import com.otterdev.service.MemoService;
import com.otterdev.service.UserService;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
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

@Path("/memos")
@ApplicationScoped
public class MemoController {
    
    @Inject
    private MemoService memoService;

    @Inject
    private JwtService jwtService;

    @Inject
    private MemoFileService memoFileService;


      // Multipart form data class
    public static class MemoMultipartForm {
        @RestForm("memo")
        @Valid
        public ReqCreateUpdateMemoDto memo;
        
        @RestForm("files")
        public List<FileUpload> files;
    }

    public static class MemoMultiPartFormUploadOnlyFile{
        @RestForm("files")
        public List<FileUpload> files;
    }

    @POST
    @Path("/")
    @RolesAllowed("user")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Blocking // Required for file uploads
    public Uni<Response> createMemo(@BeanParam MemoMultipartForm form) {
        
        // Validate form data
        if (form == null || form.memo == null) {
            ErrorResponse errorResponse = new ErrorResponse("Validation Error", "Memo data is required");
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build());
        }
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        // Create the memo first
        return memoService.newMemo(form.memo, userId)
            .chain(memoResult -> {
                if (memoResult.isLeft()) {
                    ServiceError error = memoResult.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Memo Creation Failed", error.message());
                    return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build());
                }
                
                Memo newMemo = memoResult.getRight();
                
                // If files are provided, upload them
                if (form.files != null && !form.files.isEmpty()) {
                    return memoFileService.uploadAllFileToMemoId(form.files, newMemo, userId)
                        .map(uploadResult -> {
                            if (uploadResult.isLeft()) {
                                ServiceError uploadError = uploadResult.getLeft();
                                ErrorResponse uploadErrorResponse = new ErrorResponse("File Upload Failed", 
                                    "Memo created but file upload failed: " + uploadError.message());
                                return Response.status(Response.Status.PARTIAL_CONTENT)
                                    .entity(uploadErrorResponse)
                                    .build();
                            }
                            
                            SuccessResponse successResponse = new SuccessResponse("Memo created successfully with files", newMemo);
                            return Response.status(Response.Status.CREATED)
                                .entity(successResponse)
                                .build();
                        });
                } else {
                    // No files to upload, return success for memo creation
                    SuccessResponse successResponse = new SuccessResponse("Memo created successfully", newMemo);
                    return Uni.createFrom().item(Response.status(Response.Status.CREATED)
                        .entity(successResponse)
                        .build());
                }
            });
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateMemo(@PathParam("id") UUID memoId, @Valid ReqCreateUpdateMemoDto memoDto) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return memoService.editMemo(memoId, memoDto, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Memo Update Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                Memo updatedMemo = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Memo updated successfully", updatedMemo);
                return Response.ok(successResponse).build();
            });
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteMemo(@PathParam("id") UUID memoId) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return memoService.deleteMemo(memoId, userId)
            .chain(result -> {  // Changed from .map() to .chain()
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Memo Deletion Failed", error.message());
                    return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build());
                }
                
                // Memo deleted successfully, now delete associated files
                return memoFileService.deleteAllFilesByMemoId(memoId, userId)
                    .map(fileResult -> {  // Now .map() is correct here
                        if (fileResult.isLeft()) {
                            ServiceError fileError = fileResult.getLeft();
                            ErrorResponse fileErrorResponse = new ErrorResponse("File Deletion Failed", 
                                "Memo deleted but file deletion failed: " + fileError.message());
                            return Response.status(Response.Status.PARTIAL_CONTENT)
                                .entity(fileErrorResponse)
                                .build();
                        }
                        
                        SuccessResponse successResponse = new SuccessResponse("Memo deleted successfully", null);
                        return Response.ok(successResponse).build();
                    });
            });
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getMemoById(@PathParam("id") UUID memoId) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return memoService.viewMemoById(memoId, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Memo Retrieval Failed", error.message());
                    return Response.status(Response.Status.NOT_FOUND)
                        .entity(errorResponse)
                        .build();
                }
                
                Memo memo = result.getRight();
                SuccessResponse successResponse = new SuccessResponse("Memo retrieved successfully", memo);
                return Response.ok(successResponse).build();
            });
    }

    @GET
    @Path("/")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getUserMemos() {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return memoService.viewAllUserMemos(userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Memos Retrieval Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                SuccessResponse successResponse = new SuccessResponse("Memos retrieved successfully", result.getRight());
                return Response.ok(successResponse).build();
            });
    }

    @GET
    @Path("/by-memo-type/{memoTypeId}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getMemosByMemoType(@PathParam("memoTypeId") UUID memoTypeId) {
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();
        
        return memoService.viewAllMemoByMemoTypeId(memoTypeId, userId)
            .map(result -> {
                if (result.isLeft()) {
                    ServiceError error = result.getLeft();
                    ErrorResponse errorResponse = new ErrorResponse("Memos by Type Retrieval Failed", error.message());
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
                }
                
                SuccessResponse successResponse = new SuccessResponse("Memos by type retrieved successfully", result.getRight());
                return Response.ok(successResponse).build();
            });
    }

    @POST
    @Path("/{memoId}/upload-file")
    @RolesAllowed("user")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Blocking // Required for file uploads
    public Uni<Response> uploadFileToMemoId(@PathParam("memoId") UUID memoId, @BeanParam MemoMultiPartFormUploadOnlyFile fileForm) {
        
        // Validate input
        if (fileForm == null || fileForm.files == null || fileForm.files.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Validation Error", "Files are required");
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build());
        }
        
        // Extract user ID from JWT token
        Optional<UUID> userIdOpt = jwtService.getCurrentUserId();
        if (userIdOpt.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Authentication Error", "Invalid or missing user ID in token");
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .build());
        }
        UUID userId = userIdOpt.get();

        // Get memo by memoId to validate it exists and user has access
        return memoService.viewMemoById(memoId, userId)
            .chain(memoResult -> {
                if (memoResult.isLeft()) {
                    ServiceError memoError = memoResult.getLeft();
                    ErrorResponse memoErrorResponse = new ErrorResponse("Memo Not Found", 
                        "Cannot upload files to memo: " + memoError.message());
                    return Uni.createFrom().item(Response.status(Response.Status.NOT_FOUND)
                        .entity(memoErrorResponse)
                        .build());
                }
                
                Memo memo = memoResult.getRight();
                
                // Upload files to the memo
                return memoFileService.uploadAllFileToMemoId(fileForm.files, memo, userId)
                    .map(uploadResult -> {
                        if (uploadResult.isLeft()) {
                            ServiceError uploadError = uploadResult.getLeft();
                            ErrorResponse uploadErrorResponse = new ErrorResponse("File Upload Failed", uploadError.message());
                            return Response.status(Response.Status.BAD_REQUEST)
                                .entity(uploadErrorResponse)
                                .build();
                        }

                        SuccessResponse successResponse = new SuccessResponse("Files uploaded successfully to memo", null);
                        return Response.status(Response.Status.CREATED)
                            .entity(successResponse)
                            .build();
                    });
            });
    }

} // end class
