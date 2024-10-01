package com.j9.bestmoments.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Slf4j
@Service
public class FfmpegService {

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    @Async
    public void encodeVideo(String inputFilePath, String outputFilePath, String resolution) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    ffmpegPath, "-i", inputFilePath, "-s", resolution, "-codec:v", "libx264", outputFilePath
            );

            Process process = processBuilder.start();

            // 로그 스트림 처리
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);  // 로그를 출력하거나 저장할 수 있습니다.
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("비디오 인코딩 실패");
            }

            log.info("인코딩 완료 : {}", outputFilePath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getVideoResolution(String inputFilePath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    ffmpegPath, "-i", inputFilePath
            );

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            String resolution = null;

            // FFmpeg의 출력에서 해상도를 파싱하여 추출
            while ((line = reader.readLine()) != null) {
                if (line.contains("Video:")) {
                    String[] parts = line.split(",");
                    for (String part : parts) {
                        if (part.trim().matches("\\d{2,}x\\d{2,}")) {
                            resolution = part.trim(); // 해상도 부분을 찾음
                            break;
                        }
                    }
                }
            }

            process.waitFor();
            return resolution;

        } catch (Exception e) {
            e.printStackTrace();
            return "해상도 파싱 실패";
        }
    }

}