package com.example.PPQ.ServiceImp;

import com.example.PPQ.Service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service
public class FileStorageServiceImp implements FileStorageService {
    @Value("${app.upload-file.base-path}")
    private String uploadBasePath;

    @Override
    public String saveFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File rỗng hoặc không tồn tại.");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null) {
            throw new RuntimeException("Tên file null.");
        }

        String fileName = UUID.randomUUID() + "_" + originalName;

        try {
            System.out.println("uploadBasePath = " + uploadBasePath);

            Path uploadPath = Paths.get(uploadBasePath, folder);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể lưu file: " + e.getMessage(), e);
        }
    }


    @Override
    public void deleteFile(String folder, String fileName) {
        try {
            URI baseUri = new URI(uploadBasePath);
            Path path = Paths.get(baseUri.getPath(), folder, fileName);
            Files.deleteIfExists(path);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
