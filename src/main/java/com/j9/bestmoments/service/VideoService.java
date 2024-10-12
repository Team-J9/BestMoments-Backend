package com.j9.bestmoments.service;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.domain.VideoStatus;
import com.j9.bestmoments.dto.request.VideoCreateDto;
import com.j9.bestmoments.dto.request.VideoUpdateDto;
import com.j9.bestmoments.repository.VideoRepository;
import com.j9.bestmoments.service.storageService.LocalStorageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final LocalStorageService storageService;
    private final FfmpegService ffmpegService;

    private final static int[] STANDARD_RESOLUTION_HEIGHTS = {144, 240, 360, 480, 720, 1080, 1440, 2160};

    @Transactional
    public Video upload(Member member, VideoCreateDto createDto) {
        Video video = Video.builder()
                .uploader(member)
                .videoStatus(createDto.videoStatus())
                .title(createDto.title())
                .description(createDto.description())
                .build();

        // 원본 영상
        String originVideoName = FileNameProvider.generateVideoFileName(video, createDto.video());
        String originVideoUrl = storageService.uploadFile(createDto.video(), originVideoName);
        video.setVideoUrl(originVideoUrl);

        // 썸네일 이미지
        String thumbnailName = FileNameProvider.generateThumbnailImageFileName(video, createDto.thumbnail());
        String thumbnailUrl = storageService.uploadFile(createDto.thumbnail(), thumbnailName);
        video.setThumbnailUrl(thumbnailUrl);

        // 원본 사이즈 인코딩
        String originalResolution = ffmpegService.getVideoResolution(originVideoUrl);
        String originalSizeEncodedVideoUrl = uploadEncodedVideo(originVideoUrl, originalResolution);

        int originalWidth = Integer.parseInt(originalResolution.split("x")[0]);
        int originalHeight = Integer.parseInt(originalResolution.split("x")[1]);

        for (int height : STANDARD_RESOLUTION_HEIGHTS) {
            // 원본 화질보다 작은 화질로만 인코딩
            if (height >= originalHeight) {
                break;
            }
            int width = originalWidth * height / originalHeight;
            String resolution = width + "x" + height;
            String encodedVideoUrl = uploadEncodedVideo(originVideoUrl, resolution);
            video.addEncodedVideoUrl(encodedVideoUrl);
        }

        // 가장 큰 원본 화질은 가장 마지막에 추가
        video.addEncodedVideoUrl(originalSizeEncodedVideoUrl);

        videoRepository.save(video);
        return video;
    }

    private String uploadEncodedVideo(String videoUrl, String resolution) {
        String encodedVideoUrl = FileNameProvider.generateEncodedVideoFileName(videoUrl, resolution);
        ffmpegService.encodeVideo(videoUrl, encodedVideoUrl, resolution);
        return encodedVideoUrl;
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
        return videoRepository.findByIdAndUploaderIdAndIsDeletedTrue(id, memberId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Video findPublicById(UUID id) {
        return videoRepository.findByIdAndVideoStatusAndIsDeletedFalse(id, VideoStatus.PUBLIC)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Video update(Video video, VideoUpdateDto updateDto) {
        if (updateDto.thumbnail() != null) {
            String thumbnailName = FileNameProvider.generateThumbnailImageFileName(video, updateDto.thumbnail());
            String thumbnailUrl = storageService.uploadFile(updateDto.thumbnail(), thumbnailName);
            video.setThumbnailUrl(thumbnailUrl);
        }
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
        return videoRepository.findAllByUploaderIdAndIsDeletedFalse(
                memberId,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        );
    }

    public Page<Video> findAllDeletedByUploaderId(UUID memberId, Pageable pageable) {
        return videoRepository.findAllByUploaderIdAndIsDeletedTrue(
                memberId,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        );
    }

    public Page<Video> findAllPublicByUploaderId(UUID memberId, Pageable pageable) {
        return videoRepository.findAllByUploaderIdAndVideoStatusAndIsDeletedFalse(
                memberId,
                VideoStatus.PUBLIC,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        );
    }

    public Page<Video> findAllPublicByTag(String tag, Pageable pageable) {
        return videoRepository.findAllByTagsContainsAndVideoStatusAndIsDeletedFalse(
                tag,
                VideoStatus.PUBLIC,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        );
    }

    public Page<Video> findAllByUploaderAndTag(UUID memberId, String tag, Pageable pageable) {
        return videoRepository.findAllByUploaderIdAndTagsContainsAndIsDeletedFalse(
                memberId,
                tag,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        );
    }

}
