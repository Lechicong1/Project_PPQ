package com.example.PPQ.Service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String saveFile(MultipartFile file, String folder);
    void deleteFile(String folder, String fileName);
}
