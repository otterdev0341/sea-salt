package com.otterdev.infrastructure.repository;

import java.util.Optional;

import com.otterdev.domain.entity.FileType;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileTypeRepository implements PanacheRepository<FileType> {
    
    // Add methods to interact with FileType entities as needed
    // For example, you might want to find a FileType by its name or ID

    public Uni<Optional<FileType>> findByDetail(String detail) {
        return find("detail", detail).firstResult()
            .map(Optional::ofNullable);
    }

    public Uni<FileType> getFileTypeByExtention(String extention) {
        String ext = extention.toLowerCase().trim();
        
        // Image file types
        if (ext.matches("^(jpg|jpeg|png|gif|svg|webp|image)$")) {
            return getImageFileType();
        }
        
        // PDF files
        if (ext.equals("pdf")) {
            return getImagePdfType();
        }
        
        // All other file types
        return getImageOtherType();
    }

    // -- internal funtions to get specific file types
    // These methods can be modified to suit your application's needs

    
    private Uni<FileType> getImageFileType() {
        return find("detail", "image").firstResult()
            .onItem().ifNull().failWith(() -> new RuntimeException("Image file type not found"));
    }

    private Uni<FileType> getImagePdfType() {
        return find("detail", "pdf").firstResult()
            .onItem().ifNull().failWith(() -> new RuntimeException("Image file type not found"));
    }

    private Uni<FileType> getImageOtherType() {
        return find("detail", "other").firstResult()
            .onItem().ifNull().failWith(() -> new RuntimeException("Image file type not found"));
    }

    
}
