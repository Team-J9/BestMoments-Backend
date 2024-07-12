package com.j9.bestmoments.service;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.MemberRole;
import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.domain.VideoStatus;
import com.j9.bestmoments.dto.request.VideoCreateDto;
import com.j9.bestmoments.dto.request.VideoUpdateDto;
import com.j9.bestmoments.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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

    public Page<Video> findAll(Pageable pageable) {
        return videoRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                )
        );
    }

    public Video findById(UUID id) {
        return videoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Video findByIdAndUploaderId(UUID id, UUID memberId) {
        return videoRepository.findByIdAndUploaderId(id, memberId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Video findDeletedByIdAndUploaderId(UUID id, UUID memberId) {
        return videoRepository.findByIdAndUploaderIdAndDeletedAtIsNotNull(id, memberId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Video findPublicById(UUID id) {
        return videoRepository.findByIdAndVideoStatus(id, VideoStatus.PUBLIC)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Video update(Video video, VideoUpdateDto updateDto) {
        video.setTitle(updateDto.title());
        video.setDescription(updateDto.description());
        video.setVideoStatus(updateDto.videoStatus());
        videoRepository.save(video);
        return video;
    }

    @Transactional
    public Video softDelete(Video video) {
        video.softDelete();
        videoRepository.save(video);
        return video;
    }

    @Transactional
    public Video restore(Video video) {
        video.restore();
        videoRepository.save(video);
        return video;
    }

    @Transactional
    public void setVideoTags(Video video, List<String> tags) {
        video.setVideoTags(tags);
        videoRepository.save(video);
    }

    public Page<Video> findAllActivatedByUploaderId(UUID memberId, Pageable pageable) {
        return videoRepository.findAllByUploaderIdAndDeletedAtIsNull(
                memberId,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        );
    }

    public Page<Video> findAllDeletedByUploaderId(UUID memberId, Pageable pageable) {
        return videoRepository.findAllByUploaderIdAndDeletedAtIsNotNull(
                memberId,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        );
    }

    public Page<Video> findAllPublicByUploaderId(UUID memberId, Pageable pageable) {
        return videoRepository.findAllByUploaderIdAndVideoStatusAndDeletedAtIsNull(
                memberId,
                VideoStatus.PUBLIC,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        );
    }

}
