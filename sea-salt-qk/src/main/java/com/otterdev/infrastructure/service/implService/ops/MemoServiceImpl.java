package com.otterdev.infrastructure.service.implService.ops;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.FileType;
import com.otterdev.domain.entity.Memo;
import com.otterdev.domain.entity.relation.MemoFileDetail;
import com.otterdev.domain.entity.User;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.memo.ReqCreateMemoDto;
import com.otterdev.domain.valueObject.dto.memo.ReqUpdateMemoDto;
import com.otterdev.error_structure.RepositoryError;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.FileDetailRepository;
import com.otterdev.infrastructure.repository.FileTypeRepository;
import com.otterdev.infrastructure.repository.MemoFileDetailRepository;
import com.otterdev.infrastructure.repository.MemoRepository;
import com.otterdev.infrastructure.repository.MemoTypeRepository;
import com.otterdev.infrastructure.repository.UserRepository;
import com.otterdev.infrastructure.repository.internal.CloudFlareR2Repository;
import com.otterdev.infrastructure.service.internal.ops.InternalMemoService;
import com.otterdev.infrastructure.service.internal.support.InternalFileRelateService;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
class MemoServiceImpl implements InternalMemoService, InternalFileRelateService {

    private final MemoRepository memoRepository;
    private final CloudFlareR2Repository cloudFlareR2Repository;
    private final MemoTypeRepository memoTypeRepository;
    private final UserRepository userRepository;
    private final FileDetailRepository fileDetailRepository;
    private final MemoFileDetailRepository memoFileDetailRepository;
    private final FileTypeRepository fileTypeRepository;

    @Inject
    public MemoServiceImpl(MemoRepository memoRepository, CloudFlareR2Repository cloudFlareR2Repository, MemoTypeRepository memoTypeRepository, UserRepository userRepository, FileDetailRepository fileDetailRepository, MemoFileDetailRepository memoFileDetailRepository, FileTypeRepository fileTypeRepository) {
        this.memoRepository = memoRepository;
        this.cloudFlareR2Repository = cloudFlareR2Repository;
        this.memoTypeRepository = memoTypeRepository;
        this.userRepository = userRepository;
        this.fileDetailRepository = fileDetailRepository;
        this.memoFileDetailRepository = memoFileDetailRepository;
        this.fileTypeRepository = fileTypeRepository;
    }
    
    
    


    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Memo>> createMemo(ReqCreateMemoDto reqCreateMemoDto, UUID userId) {
        // Create new memo instance with basic info
        Memo newMemo = new Memo();
        LocalDateTime now = LocalDateTime.now();
        newMemo.setName(reqCreateMemoDto.getName().trim());
        newMemo.setDetail(reqCreateMemoDto.getDetail());
        newMemo.setCreatedAt(now);
        newMemo.setUpdatedAt(now);

        // Step 1: Validate and get user
        return userRepository.findByUserId(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("User not found"))
                    );
                }
                newMemo.setCreatedBy(userOpt.get());

                return memoTypeRepository.findByIdAndUserId(reqCreateMemoDto.getMemoType(), userOpt.get().getId())
                    .chain(memoOps -> {
                        if (memoOps.isEmpty()) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.NotFound("Memo type not found"))
                            );
                        }
                        newMemo.setMemoType(memoOps.get());

                        // Step 2: Persist the memo
                        return memoRepository.persist(newMemo)
                            .chain(persistedMemo -> {
                                // case A No file
                                if (reqCreateMemoDto.getFiles() == null || reqCreateMemoDto.getFiles().isEmpty()) {
                                    return Uni.createFrom().item(Either.right(persistedMemo));
                                } else {
                                    // case B Loop through files
                                    return Uni.combine().all().unis(
                                        reqCreateMemoDto.getFiles().stream()
                                            .map(file -> uploadAndCreateFileDetail(file, persistedMemo, userOpt.get()))
                                            .toList()
                                        ).with(results -> {
                                            // Check if all uploads were successful
                                            boolean allSuccess = results.stream().allMatch(result -> ((Either<ServiceError, FileDetail>) result).isRight());
                                            if (allSuccess) {
                                                return Either.right(persistedMemo);
                                            } else {
                                                // If any upload failed, return the first error
                                                ServiceError firstError = results.stream()
                                                    .filter(result -> ((Either<?, ?>) result).isLeft())
                                                    .map(result -> ((Either<ServiceError, ?>) result).getLeft())
                                                    .findFirst()
                                                    .orElse(new ServiceError.OperationFailed("Some files failed to upload"));
                                                return Either.left(firstError);
                                            }
                                        });
                                }
                                
                                
                            });
                    });
            });
    }

    // Helper method to upload file and create file detail
    private Uni<Either<ServiceError, MemoFileDetail>> uploadAndCreateFileDetail(
        FileUpload file, 
        Memo memo, 
        User user
    ) {
        // upload file to CloudFlare R2
        // use file to check file type repository
        // use fileR2dto to persist FileDetail and MemoFileDetail

        return cloudFlareR2Repository.uploadFile(file)
            .chain(uploadResult -> uploadResult.fold(
                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed(error.message()))), // Pass through the error
                fileR2Dto -> {
                    // Create and persist FileDetail first
                    FileDetail fileDetail = new FileDetail();
                    // set fine type by using fileTypeRepository
                    fileTypeRepository.getFileTypeByExtention(file.contentType());
                    fileDetail.setName(file.fileName());
                    fileDetail.setObjectKey(fileR2Dto.getObjectKey());
                    fileDetail.setPath(fileR2Dto.getFileUrl());
                    fileDetail.setSize(file.size());
                    fileDetail.setCreatedBy(user);
                    fileDetail.setCreatedAt(LocalDateTime.now());
                    fileDetail.setUpdatedAt(LocalDateTime.now());
                    

                    return fileTypeRepository.getFileTypeByExtention(file.contentType())
                        .chain(fileType -> {
                            
                            fileDetail.setType(fileType);
                            return fileDetailRepository.persist(fileDetail)
                                .chain(persistedFileDetail -> {
                                    // Create MemoFileDetail relation
                                    MemoFileDetail memoFileDetail = new MemoFileDetail();
                                    memoFileDetail.setMemo(memo);
                                    memoFileDetail.setFile(persistedFileDetail);
                                    memoFileDetail.setCreatedBy(user);
                                    memoFileDetail.setCreatedAt(LocalDateTime.now());

                                    // Persist the relation
                                    return memoFileDetailRepository.persist(memoFileDetail)
                                        .map(persistedMemoFileDetail -> Either.<ServiceError, MemoFileDetail>right(persistedMemoFileDetail));
                                });
                        });
                }
            ));
    }// end funtion

    @Override
    public Uni<Either<ServiceError, Memo>> updateMemo(UUID memoId, ReqUpdateMemoDto reqUpdateMemoDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMemo'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteMemo(UUID memoId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteMemo'");
    }

    @Override
    public Uni<Either<ServiceError, Memo>> getMemoById(UUID memoId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMemoById'");
    }

    @Override
    public Uni<Either<ServiceError, List<Memo>>> getAllMemos(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllMemos'");
    }

    @Override
    public Uni<Either<ServiceError, List<Memo>>> getMemosByType(UUID memoTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMemosByType'");
    }

    @Override
    public Uni<Either<ServiceError, List<Memo>>> getMemosByPropertyId(UUID propertyId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMemosByPropertyId'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> addFileToMemoById(UUID memoId, RequestAttachFile file, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFileToMemoById'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteFileFromMemoById(UUID memoId, UUID fileId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFileFromMemoById'");
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
