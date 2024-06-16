package org.person.myapp.Services;

import org.person.myapp.Models.S3Model.ServiceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {
    ServiceResponse uploadFile(MultipartFile file) throws IOException;

    ServiceResponse getDownloadUrl(String fileName);

    ServiceResponse deleteFile(String fileName);
}
