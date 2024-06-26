package org.person.myapp.Services;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/*
Fahim created at 6/5/2021
*/
@Service
@Log4j2
public class AmazonS3Client {

    private AmazonS3 s3client;

    @PostConstruct
    private void initializeAmazon() {
        this.s3client =
                AmazonS3ClientBuilder
                        .standard()
                        .withRegion(Regions.EU_NORTH_1)
                        .withCredentials(getAwsCredentialProvider())
                        .build();
    }

    private AWSCredentialsProvider getAwsCredentialProvider() {
        AWSCredentials awsCredentials =
                new BasicAWSCredentials("Redacted",
                        "Redacted");
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

    public PutObjectResult uploadFile(String bucketName, MultipartFile file) throws IOException {
        if (!s3client.doesBucketExist(bucketName)) {
            log.info("Bucket is not available");
            s3client.createBucket(bucketName);
            log.info(bucketName + " created");
        }

        String fileName = file.getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType("application/octet-stream");


        PutObjectResult result = s3client.putObject(
                new PutObjectRequest(bucketName, fileName, file.getInputStream(), objectMetadata));

        // Set file access to private
        s3client.setObjectAcl(bucketName, fileName, CannedAccessControlList.BucketOwnerFullControl);

        log.info("File Uploaded successfully");

        return result;
    }
    public PutObjectResult uploadFile64(String bucketName,String filename, byte[] imageBytes) throws IOException {
        if (!s3client.doesBucketExist(bucketName)) {
            log.info("Bucket is not available");
            s3client.createBucket(bucketName);
            log.info(bucketName + " created");
        }
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(inputStream.available());
        objectMetadata.setContentType("application/octet-stream");


        PutObjectResult result = s3client.putObject(
                new PutObjectRequest(bucketName, filename, inputStream, objectMetadata));

        // Set file access to private
        s3client.setObjectAcl(bucketName, filename, CannedAccessControlList.BucketOwnerFullControl);

        log.info("File Uploaded successfully");

        return result;
    }

    public String getPreSignedDownloadUrl(String bucketName, String fileName) {

        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; //1 HR expiration time
        expiration.setTime(expTimeMillis);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
        log.info("Pre-signed URL found for " + fileName);
        return url.toString();
    }

    public String getPublicDownloadUrl(String bucketName, String fileName) {
        s3client.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);
        URL url = s3client.getUrl(bucketName, fileName);
        return url.toString();
    }

    public void deleteFile(String bucketName, String fileName) {
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        log.info(fileName + " deleted");
    }


}
