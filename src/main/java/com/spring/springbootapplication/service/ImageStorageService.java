package com.spring.springbootapplication.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {

  private final Path uploadDir =
    Paths.get("uploads").toAbsolutePath().normalize();

  public String save(MultipartFile imageFile) {

    if (imageFile == null || imageFile.isEmpty()) {
      return null;
    }

    String originalFilename = imageFile.getOriginalFilename();

    String extension =
      StringUtils.getFilenameExtension(originalFilename);

    String fileName = UUID.randomUUID().toString();

    if (extension != null && !extension.isBlank()) {
        fileName += "." + extension.toLowerCase();
    }

    try {
      Files.createDirectories(uploadDir);

      Path destination =
        uploadDir.resolve(fileName).normalize();

      Files.copy(
        imageFile.getInputStream(),
        destination,
        StandardCopyOption.REPLACE_EXISTING
      );

      System.out.println("=== 画像保存確認 ===");
      System.out.println("uploadDir: " + uploadDir);
      System.out.println("destination: " + destination);
      System.out.println("exists: " + Files.exists(destination));
      System.out.println("size: " + Files.size(destination));

    } catch (IOException e) {
      throw new IllegalStateException(
        "画像の保存に失敗しました",
        e
      );
    }

    return "/uploads/" + fileName;
  }
}