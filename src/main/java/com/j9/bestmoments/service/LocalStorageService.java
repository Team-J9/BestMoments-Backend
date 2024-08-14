package com.j9.bestmoments.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class LocalStorageService implements StorageService {

    @Value("${storage.local.dir}")
    private String dir;

    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            Path path = Paths.get(dir + fileName);
            if (!path.toFile().exists()) {
                path.toFile().mkdirs();
            }
            File destinationFile = new File(dir, fileName);
            file.transferTo(destinationFile);
            return destinationFile.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            return "파일 업로드에 실패했습니다.";
        }
    }
}
