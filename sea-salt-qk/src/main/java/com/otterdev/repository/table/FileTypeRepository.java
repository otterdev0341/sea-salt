package com.otterdev.repository.table;

import java.util.Set;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.otterdev.entity.table.FileType;
import com.otterdev.error.repository.FetchFailedError;
import com.otterdev.error.repository.NotFoundError;
import com.otterdev.error.repository.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class FileTypeRepository implements PanacheRepository<FileType> {

    // File type constants - these should match your predefined values in the database
    private static final String FILE_TYPE_IMAGE = "image";
    private static final String FILE_TYPE_PDF = "pdf";
    private static final String FILE_TYPE_OTHER = "other";

    // Supported image extensions
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");
    private static final Set<String> PDF_EXTENSIONS = Set.of("pdf");

    // This class can be used to define custom query methods if needed.
    // For example, you can add methods like findByTypeName, findByFileExtension, etc.
    public Uni<Either<RepositoryError, FileType>> findByDescription(String description) {
        return find("description", description.trim().toLowerCase()).<FileType>firstResult()
                .onItem().<Either<RepositoryError, FileType>>transform(fileType -> {
                    if (fileType == null) {
                        return Either.<RepositoryError, FileType>left(
                            new NotFoundError("FileType not found with description: " + description)
                        );
                    }
                    return Either.<RepositoryError, FileType>right(fileType);
                })
                .onFailure().recoverWithItem(throwable -> 
                    Either.<RepositoryError, FileType>left(
                        new FetchFailedError("Failed to fetch FileType by description: " + throwable.getMessage())
                    )
                );
    }

    public Uni<Either<RepositoryError, FileType>> detemineFileType(FileUpload targetFile) {
        return Uni.createFrom().item(() -> {
            try {
                // Validate input
                if (targetFile == null || targetFile.fileName() == null) {
                    return Either.<RepositoryError, String>left(
                        new FetchFailedError("FileUpload or filename is null")
                    );
                }

                // Get file extension
                String fileName = targetFile.fileName().toLowerCase();
                String extension = getFileExtension(fileName);
                
                // Determine file type based on extension
                String fileType = determineFileTypeByExtension(extension);
                
                return Either.<RepositoryError, String>right(fileType);
                
            } catch (Exception e) {
                return Either.<RepositoryError, String>left(
                    new FetchFailedError("Failed to determine file type: " + e.getMessage())
                );
            }
        }).chain(result -> {
            if (result.isLeft()) {
                return Uni.createFrom().item(Either.<RepositoryError, FileType>left(result.getLeft()));
            }
            
            String fileTypeString = result.getRight();
            
            // Search for existing FileType object by description
            return findByDescription(fileTypeString);
        });
    }

    /**
     * Extracts file extension from filename
     * @param fileName The filename
     * @return String extension without dot, or empty string if no extension
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * Determines file type category based on extension
     * @param extension File extension (without dot)
     * @return String file type category
     */
    private String determineFileTypeByExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return FILE_TYPE_OTHER;
        }

        extension = extension.toLowerCase();
        
        if (IMAGE_EXTENSIONS.contains(extension)) {
            return FILE_TYPE_IMAGE;
        } else if (PDF_EXTENSIONS.contains(extension)) {
            return FILE_TYPE_PDF;
        } else {
            return FILE_TYPE_OTHER;
        }
    }
}
