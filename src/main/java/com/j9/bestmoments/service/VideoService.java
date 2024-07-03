package com.j9.bestmoments.service;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.dto.request.VideoCreateDto;
import com.j9.bestmoments.dto.response.VideoFindDto;
import com.j9.bestmoments.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final StorageService storageService;

    @Transactional
    public Video upload(Member member, VideoCreateDto createDto) {
        String fileUrl = storageService.uploadFile(createDto.file());
        Video video = Video.builder()
                .fileUrl(fileUrl)
                .uploader(member)
                .videoStatus(createDto.videoStatus())
                .title(createDto.title())
                .description(createDto.description())
                .build();
        videoRepository.save(video);
        return video;
    }


}
