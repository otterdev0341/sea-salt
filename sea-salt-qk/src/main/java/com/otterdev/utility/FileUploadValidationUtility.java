package com.otterdev.utility;

import java.util.List;
import java.util.Set;

import org.jboss.resteasy.reactive.multipart.FileUpload;

public class FileUploadValidationUtility {
    public static boolean validateFiles(List<FileUpload> files) {
    if (files == null || files.isEmpty()) {
        return true;  // No files is valid
    }
    
    // Maximum file size (10MB)
    long maxFileSize = 10 * 1024 * 1024;
    
    // Allowed MIME types
    Set<String> allowedTypes = Set.of(
        "image/jpeg",
        "image/png",
        "image/gif",
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    
    for (FileUpload file : files) {
        if (file.size() > maxFileSize) {
            return false;
        }
        if (!allowedTypes.contains(file.contentType())) {
            return false;
        }
    }
    
    return true;
}
}
