package org.person.myapp.Services.Impl;
import org.person.myapp.Services.*;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;




@Service
public class ImageServiceImpl implements ImageService {


    private final Path rootLocation;

    String bucketName = "s3bucket";

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    public ImageServiceImpl(ImageProperties properties) {

        if(properties.getLocation().trim().isEmpty()){
            throw new ImageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new ImageException("Failed to store empty file.");
            }
//            Path destinationFile = this.rootLocation.resolve(
//                            Paths.get(Objects.requireNonNull(file.getOriginalFilename())))
//                    .normalize().toAbsolutePath();
//            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
//                // This is a security check
//                throw new ImageException(
//                        "Cannot store file outside current directory.");
//            }
            try (InputStream inputStream = file.getInputStream()) {
                amazonS3Client.uploadFile(bucketName, file);
                return file.getOriginalFilename();
            }
        }
        catch (IOException e) {
            throw new ImageException("Failed to store file.", e);
        }
    }
    public String store64(String base64Image) {
        try {
            // Decode the base64 image
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            // Generate a unique filename (you can modify this as needed)
            String filename = UUID.randomUUID().toString();
            // Create a MultipartFile

            amazonS3Client.uploadFile64(bucketName,filename,imageBytes);
            return filename;

        } catch (IOException e) {
            throw new ImageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new ImageException("Failed to read stored files", e);
        }

    }
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new ImageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new ImageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new ImageException("Could not initialize storage", e);
        }
    }
}
