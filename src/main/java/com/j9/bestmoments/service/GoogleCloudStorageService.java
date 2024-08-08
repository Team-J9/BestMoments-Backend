package com.j9.bestmoments.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.util.FileNameGenerator;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class GoogleCloudStorageService implements StorageService {

    private final String bucketName;
    private final Storage storage;

    public GoogleCloudStorageService(
            @Value("${storage.google-cloud.bucket.name}") String bucketName,
            @Value("${storage.google-cloud.project.id}") String projectId
    ) throws IOException {
        this.bucketName = bucketName;
        this.storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(Files.newInputStream(Paths.get("credentials/bestmoments-file-uploader.json"))))
                .build()
                .getService();
    }

    // 다운로드 링크를 반환
    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        String contentType = file.getContentType().split("/")[1];
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        try {
            storage.create(blobInfo, file.getInputStream());
        }
        catch (Exception e) {

            throw new RuntimeException("IOException");
        }
        log.info("blobId = {}", blobId);
        return String.format("https://storage.cloud.google.com/%s/%s.%s", bucketName, fileName, contentType);
    }

}
