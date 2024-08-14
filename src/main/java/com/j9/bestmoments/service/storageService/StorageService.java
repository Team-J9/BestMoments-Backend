package com.j9.bestmoments.service.storageService;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String uploadFile(MultipartFile file, String fileName);

}
