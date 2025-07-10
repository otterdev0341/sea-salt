package com.otterdev.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.otterdev.dto.cloudflare.ResFileR2Dto;
import com.otterdev.entity.relation.FileUser;
import com.otterdev.entity.relation.MemoFile;
import com.otterdev.entity.table.FileDetail;
import com.otterdev.entity.table.FileType;
import com.otterdev.entity.table.Memo;
import com.otterdev.entity.table.User;
import com.otterdev.error.repository.RepositoryError;
import com.otterdev.error.service.ServiceError;
import com.otterdev.error.service.UnexpectedError;
import com.otterdev.error.service.ValidationError;
import com.otterdev.repository.cloudflare.CloudflareR2Repository;
import com.otterdev.repository.relation.FileUserRepository;
import com.otterdev.repository.relation.MemoFileRepository;
import com.otterdev.repository.table.FileDetailRepository;
import com.otterdev.repository.table.FileTypeRepository;
import com.otterdev.repository.table.MemoRepository;
import com.otterdev.repository.table.UserRepository;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.multipart.FileUpload;


@ApplicationScoped
public class MemoFileService {
    
    @Inject
    private FileUserRepository fileUserRepository;

    @Inject
    private MemoFileRepository memoFileRepository;

    @Inject
    private FileDetailRepository fileDetailRepository;

    @Inject
    private FileTypeRepository fileTypeRepository;

    @Inject
    private CloudflareR2Repository cloudflareR2Repository;

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private MemoRepository memoRepository;

    // memo section
    // uplodaeFileToMemoId
    /*
     * 1 accept file, accept Memo Entity
     * 
     * a. upload file to cloudflare r2
     * b. define the file type, by fileTypeRepository.detemineFileType(fileUpload)
     * c. save file detail to file_detail table
     * d. save file_detail.id to file_user table
     * e. save file_detail.id to memo_file table
     */
    @WithTransaction
    public Uni<Either<ServiceError, Void>> uploadSingleFileFileToMemoId(Memo memo, FileUpload fileUpload, User user) {
        // Validate inputs
        if (memo == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Memo cannot be null")));
        }
        
        if (fileUpload == null || fileUpload.fileName() == null || fileUpload.fileName().trim().isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("FileUpload or filename cannot be null or empty")));
        }
        
        if (fileUpload.size() <= 0) {
            return Uni.createFrom().item(Either.left(new ValidationError("File is empty")));
        }

        if (user == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("User cannot be null")));
        }

        // Step a: Upload file to cloudflare r2
        return Uni.createFrom().item(() -> {
            String fileKey = UUID.randomUUID().toString() + "-" + fileUpload.fileName();
            return fileKey;
        }).chain(fileKey -> {
            return Uni.createFrom().item(() -> {
                try {
                    // Create FileInputStream from the uploaded file
                    FileInputStream inputStream = new FileInputStream(fileUpload.uploadedFile().toFile());
                    
                    // Call the cloudflareR2Repository method and handle the response
                    Either<RepositoryError, ResFileR2Dto> uploadResult = cloudflareR2Repository.saveFile(fileKey, inputStream, fileUpload.size(), fileUpload.contentType()).
                        await().indefinitely();
                    
                    // Close the input stream
                    try {
                        inputStream.close();
                    } catch (IOException closeException) {
                        System.err.println("Warning: Failed to close input stream: " + closeException.getMessage());
                    }
                    
                    // Convert the result to proper ServiceError type
                    if (uploadResult.isLeft()) {
                        return Either.<ServiceError, ResFileR2Dto>left(
                            new UnexpectedError("Failed to upload file to Cloudflare R2: " + uploadResult.getLeft().toString())
                        );
                    }
                    
                    return Either.<ServiceError, ResFileR2Dto>right(uploadResult.getRight());
                    
                } catch (IOException e) {
                    return Either.<ServiceError, ResFileR2Dto>left(
                        new UnexpectedError("Failed to read uploaded file: " + e.getMessage(), e)
                    );
                } catch (Exception e) {
                    return Either.<ServiceError, ResFileR2Dto>left(
                        new UnexpectedError("Failed to upload file to Cloudflare R2: " + e.getMessage(), e)
                    );
                }
            });
        }).chain(r2Result -> {
            if (r2Result.isLeft()) {
                return Uni.createFrom().item(Either.<ServiceError, Void>left(r2Result.getLeft()));
            }
            
            ResFileR2Dto fileR2Dto = r2Result.getRight();
            
            // Step b: Define the file type using fileTypeRepository.detemineFineType(fileUpload)
            return fileTypeRepository.detemineFileType(fileUpload)
                .chain(fileTypeResult -> {
                    if (fileTypeResult.isLeft()) {
                        // Convert RepositoryError to ServiceError
                        return Uni.createFrom().item(Either.<ServiceError, Void>left(
                            new UnexpectedError("Failed to determine file type: " + fileTypeResult.getLeft().message())
                        ));
                    }
                    
                    FileType fileType = fileTypeResult.getRight();
                    LocalDateTime now = LocalDateTime.now();
                    
                    // Step c: Save file detail to file_detail table
                    FileDetail fileDetail = FileDetail.builder()
                        .objectKey(fileR2Dto.getObjectKey())
                        .url(fileR2Dto.getFileUrl())
                        .contentType(fileType)
                        .size(String.valueOf(fileUpload.size()))
                        .uploadedBy(user)
                        .uploadedAt(now)
                        .build();
                    
                    return fileDetailRepository.persist(fileDetail)
                        .chain(savedFileDetail -> {
                            // Step d: Save file_detail.id to file_user table
                            // Use the User parameter instead of memo.getCreatedBy()
                            FileUser fileUser = FileUser.builder()
                                .fileId(savedFileDetail)
                                .userId(user)
                                .build();
                            
                            return fileUserRepository.persist(fileUser)
                                .chain(savedFileUser -> {
                                    // Step e: Save file_detail.id to memo_file table
                                    MemoFile memoFile = MemoFile.builder()
                                        .memoId(memo)
                                        .fileId(savedFileDetail)
                                        .build();
                                    
                                    return memoFileRepository.persist(memoFile)
                                        .map(savedMemoFile -> Either.<ServiceError, Void>right(null))
                                        .onFailure().recoverWithItem(throwable -> 
                                            Either.<ServiceError, Void>left(
                                                new UnexpectedError("Failed to save memo file relation: " + throwable.getMessage(), throwable)
                                            )
                                        );
                                })
                                .onFailure().recoverWithItem(throwable -> 
                                    Either.<ServiceError, Void>left(
                                        new UnexpectedError("Failed to save file user relation: " + throwable.getMessage(), throwable)
                                    )
                                );
                        })
                        .onFailure().recoverWithItem(throwable -> 
                            Either.<ServiceError, Void>left(
                                new UnexpectedError("Failed to save file detail: " + throwable.getMessage(), throwable)
                            )
                        );
                });
        });
    }// end uploadSingleFileToMemoId

    // uplodateAllFileToMemoId
    /* 1 accept list of file, accept Memo Entity, accept User
     * 2 for each file
     *    a. upload file to cloudflare r2
     *    b. save file detail to file_detail table
     *    c. save file to file_user table
     *    d. save file to memo_file table
     */
    @WithTransaction
    public Uni<Either<ServiceError, Void>> uploadAllFileToMemoId(List<FileUpload> fileUploads, Memo memo, UUID userId) {
        if (fileUploads == null || fileUploads.isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("File uploads list cannot be null or empty")));
        }
        
        if (memo == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Memo cannot be null")));
        }

        Optional<User> userOpt = userRepository.findById(userId).await().indefinitely();
        
        // Process each file sequentially
        return processFilesSequentially(fileUploads, memo, userOpt.get(), 0);
    }
    
    private Uni<Either<ServiceError, Void>> processFilesSequentially(List<FileUpload> fileUploads, Memo memo, User user, int index) {
        if (index >= fileUploads.size()) {
            // All files processed successfully
            return Uni.createFrom().item(Either.right(null));
        }
        
        FileUpload currentFile = fileUploads.get(index);
        
        return uploadSingleFileFileToMemoId(memo, currentFile, user)
            .chain(result -> {
                if (result.isLeft()) {
                    // Stop processing on first error
                    return Uni.createFrom().item(result);
                }
                
                // Process next file
                return processFilesSequentially(fileUploads, memo, user, index + 1);
            });
    }

    // deleteFileFromMemoId
    /*
     * 1  accept Memo Entity, User Enity, FileDetail Entity
     * before delete check is fileDetail is belong to user
     * get object key from fileDetail
     * a. delete file from cloudflare r2 repository
     * b. delete file from memo_file table MemoFileRepository
     * c. delete file from file_user table
     * d. delete file detail from file_detail table
     */
    @WithTransaction
    public Uni<Either<ServiceError, Void>> deleteFileFromMemoId(UUID memo, FileDetail fileDetail, User user) {
        // Validate inputs
        if (memo == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Memo cannot be null")));
        }
        
        if (fileDetail == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("FileDetail cannot be null")));
        }
        
        if (user == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("User cannot be null")));
        }

        // Before delete, check if fileDetail belongs to user
        return fileUserRepository.find("fileId = ?1 and userId = ?2", fileDetail, user)
            .firstResult()
            .chain(fileUser -> {
                if (fileUser == null) {
                    return Uni.createFrom().item(Either.<ServiceError, Void>left(
                        new ValidationError("File does not belong to the specified user")
                    ));
                }
                
                // Check if file belongs to memo
                return memoFileRepository.find("memoId = ?1 and fileId = ?2", memo, fileDetail)
                    .firstResult()
                    .chain(memoFile -> {
                        if (memoFile == null) {
                            return Uni.createFrom().item(Either.<ServiceError, Void>left(
                                new ValidationError("File does not belong to the specified memo")
                            ));
                        }
                        
                        // Get object key from fileDetail
                        String objectKey = fileDetail.getObjectKey();
                        
                        // Step a: Delete file from cloudflare r2 repository
                        return cloudflareR2Repository.deleteFile(objectKey)
                            .chain(r2DeleteResult -> {
                                if (r2DeleteResult.isLeft()) {
                                    return Uni.createFrom().item(Either.<ServiceError, Void>left(
                                        new UnexpectedError("Failed to delete file from Cloudflare R2: " + r2DeleteResult.getLeft().message())
                                    ));
                                }
                                
                                // Step b: Delete file from memo_file table
                                return memoFileRepository.delete(memoFile)
                                    .chain(memoDeleteResult -> {
                                        // Step c: Delete file from file_user table
                                        return fileUserRepository.delete(fileUser)
                                            .chain(userDeleteResult -> {
                                                // Step d: Delete file detail from file_detail table
                                                return fileDetailRepository.delete(fileDetail)
                                                    .map(detailDeleteResult -> Either.<ServiceError, Void>right(null))
                                                    .onFailure().recoverWithItem(throwable -> 
                                                        Either.<ServiceError, Void>left(
                                                            new UnexpectedError("Failed to delete file detail: " + throwable.getMessage(), throwable)
                                                        )
                                                    );
                                            })
                                            .onFailure().recoverWithItem(throwable -> 
                                                Either.<ServiceError, Void>left(
                                                    new UnexpectedError("Failed to delete file user relation: " + throwable.getMessage(), throwable)
                                                )
                                            );
                                    })
                                    .onFailure().recoverWithItem(throwable -> 
                                        Either.<ServiceError, Void>left(
                                            new UnexpectedError("Failed to delete memo file relation: " + throwable.getMessage(), throwable)
                                        )
                                    );
                            });
                    });
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, Void>left(
                    new UnexpectedError("Failed to verify file ownership: " + throwable.getMessage(), throwable)
                )
            );
    }

    // ViewAllFilesByMemoId
    /*
     * 1 accept Memo Entity
     * a. get all file by memoId from memo_file table
     * b. get all file detail from file_detail table
     * c. return list of files
     */
    @WithTransaction
    public Uni<Either<ServiceError, List<FileDetail>>> viewAllFilesByMemoId(Memo memo) {
        // Validate inputs
        if (memo == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Memo cannot be null")));
        }

        // Step a: Get all files by memoId from memo_file table
        return memoFileRepository.find("memoId", memo)
            .list()
            .chain(memoFiles -> {
                if (memoFiles == null || memoFiles.isEmpty()) {
                    // No files found for this memo
                    return Uni.createFrom().item(Either.<ServiceError, List<FileDetail>>right(List.of()));
                }
                
                // Step b: Get all file details from file_detail table
                // Extract file IDs from memo files
                List<FileDetail> fileDetails = memoFiles.stream()
                    .map(MemoFile::getFileId)
                    .toList();
                
                // Step c: Return list of files
                return Uni.createFrom().item(Either.<ServiceError, List<FileDetail>>right(fileDetails));
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, List<FileDetail>>left(
                    new UnexpectedError("Failed to retrieve files for memo: " + throwable.getMessage(), throwable)
                )
            );
    }

    // deleteAllFilesByMemoId
    /*
     * list all file memo then delete in r2dbc
     * delete all files by memoId in file_memo
     * delete all file in file_user table 
     * delete all file detail in file_detail table
     * 
     */
    @WithTransaction
    public Uni<Either<ServiceError, Void>> deleteAllFilesByMemoId(UUID memoId, UUID userId) {
        // Validate inputs
        if (memoId == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Memo ID cannot be null")));
        }
        
        if (userId == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("User ID cannot be null")));
        }

        // Get User entity from userId
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Void>left(
                        new ValidationError("User not found with ID: " + userId)
                    ));
                }
                
                User user = userOpt.get();
                
                // Get Memo entity from memoId
                return memoRepository.findById(memoId)
                    .chain(memoOpt -> {
                        if (memoOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.<ServiceError, Void>left(
                                new ValidationError("Memo not found with ID: " + memoId)
                            ));
                        }
                        
                        Memo memo = memoOpt.get();
                        
                        // Step 1: List all files for this memo
                        return memoFileRepository.find("memoId", memo)
                            .list()
                            .chain(memoFiles -> {
                                if (memoFiles == null || memoFiles.isEmpty()) {
                                    // No files found for this memo
                                    return Uni.createFrom().item(Either.<ServiceError, Void>right(null));
                                }
                                
                                // Extract file details
                                List<FileDetail> fileDetails = memoFiles.stream()
                                    .map(MemoFile::getFileId)
                                    .toList();
                                
                                // Delete all files sequentially
                                return deleteFilesSequentially(fileDetails, memo, user, 0)
                                    .chain(deleteResult -> {
                                        if (deleteResult.isLeft()) {
                                            return Uni.createFrom().item(deleteResult);
                                        }
                                        
                                        // Step 2: Delete all files by memoId in memo_file table
                                        return memoFileRepository.delete("memoId", memo)
                                            .chain(memoFileDeleteCount -> {
                                                // Step 3: Delete all files in file_user table for these files
                                                return deleteFileUserRelations(fileDetails)
                                                    .chain(fileUserDeleteResult -> {
                                                        if (fileUserDeleteResult.isLeft()) {
                                                            return Uni.createFrom().item(fileUserDeleteResult);
                                                        }
                                                        
                                                        // Step 4: Delete all file details in file_detail table
                                                        return deleteFileDetails(fileDetails)
                                                            .map(fileDetailDeleteResult -> {
                                                                if (fileDetailDeleteResult.isLeft()) {
                                                                    return fileDetailDeleteResult;
                                                                }
                                                                
                                                                return Either.<ServiceError, Void>right(null);
                                                            });
                                                    });
                                            })
                                            .onFailure().recoverWithItem(throwable -> 
                                                Either.<ServiceError, Void>left(
                                                    new UnexpectedError("Failed to delete memo file relations: " + throwable.getMessage(), throwable)
                                                )
                                            );
                                    });
                            })
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, Void>left(
                                    new UnexpectedError("Failed to retrieve files for memo: " + throwable.getMessage(), throwable)
                                )
                            );
                    });
            });
    }

    // Helper method to delete files sequentially from Cloudflare R2
    private Uni<Either<ServiceError, Void>> deleteFilesSequentially(List<FileDetail> fileDetails, Memo memo, User user, int index) {
        if (index >= fileDetails.size()) {
            // All files processed successfully
            return Uni.createFrom().item(Either.right(null));
        }
        
        FileDetail currentFile = fileDetails.get(index);
        
        return deleteFileFromR2(currentFile)
            .chain(result -> {
                if (result.isLeft()) {
                    // Log error but continue with next file
                    System.err.println("Warning: Failed to delete file from R2: " + result.getLeft().message());
                }
                
                // Continue with next file even if current one failed
                return deleteFilesSequentially(fileDetails, memo, user, index + 1);
            });
    }

    // Helper method to delete a single file from Cloudflare R2
    private Uni<Either<ServiceError, Void>> deleteFileFromR2(FileDetail fileDetail) {
        try {
            String objectKey = fileDetail.getObjectKey();
            
            return cloudflareR2Repository.deleteFile(objectKey)
                .map(r2DeleteResult -> {
                    if (r2DeleteResult.isLeft()) {
                        return Either.<ServiceError, Void>left(
                            new UnexpectedError("Failed to delete file from Cloudflare R2: " + r2DeleteResult.getLeft().message())
                        );
                    }
                    
                    return Either.<ServiceError, Void>right(null);
                });
        } catch (Exception e) {
            return Uni.createFrom().item(Either.<ServiceError, Void>left(
                new UnexpectedError("Failed to delete file from R2: " + e.getMessage(), e)
            ));
        }
    }

    // Helper method to delete file_user relations
    private Uni<Either<ServiceError, Void>> deleteFileUserRelations(List<FileDetail> fileDetails) {
        if (fileDetails.isEmpty()) {
            return Uni.createFrom().item(Either.right(null));
        }
        
        return deleteFileUserRelationsSequentially(fileDetails, 0);
    }

    private Uni<Either<ServiceError, Void>> deleteFileUserRelationsSequentially(List<FileDetail> fileDetails, int index) {
        if (index >= fileDetails.size()) {
            return Uni.createFrom().item(Either.right(null));
        }
        
        FileDetail currentFile = fileDetails.get(index);
        
        return fileUserRepository.delete("fileId", currentFile)
            .chain(deleteCount -> {
                // Continue with next file
                return deleteFileUserRelationsSequentially(fileDetails, index + 1);
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, Void>left(
                    new UnexpectedError("Failed to delete file user relation: " + throwable.getMessage(), throwable)
                )
            );
    }

    // Helper method to delete file details
    private Uni<Either<ServiceError, Void>> deleteFileDetails(List<FileDetail> fileDetails) {
        if (fileDetails.isEmpty()) {
            return Uni.createFrom().item(Either.right(null));
        }
        
        return deleteFileDetailsSequentially(fileDetails, 0);
    }

    private Uni<Either<ServiceError, Void>> deleteFileDetailsSequentially(List<FileDetail> fileDetails, int index) {
        if (index >= fileDetails.size()) {
            return Uni.createFrom().item(Either.right(null));
        }
        
        FileDetail currentFile = fileDetails.get(index);
        
        return fileDetailRepository.delete(currentFile)
            .chain(deleteResult -> {
                // Continue with next file
                return deleteFileDetailsSequentially(fileDetails, index + 1);
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, Void>left(
                    new UnexpectedError("Failed to delete file detail: " + throwable.getMessage(), throwable)
                )
            );
    }
}
