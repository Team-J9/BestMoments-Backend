package com.j9.bestmoments.controller;

import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.dto.response.VideoFindDto;
import com.j9.bestmoments.dto.response.VideoPreviewDto;
import com.j9.bestmoments.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
@Tag(name = "Video", description = "동영상 관련 API")
public class VideoController {

    private final VideoService videoService;

    @Operation(summary = "동영상 열람")
    @GetMapping("/{videoId}")
    public ResponseEntity<VideoFindDto> view(@PathVariable UUID videoId) {
        Video video = videoService.findPublicById(videoId);
        return ResponseEntity.ok(VideoFindDto.of(video));
    }

    @Operation(summary = "사용자의 동영상 목록 조회")
    @GetMapping("/by/{memberId}")
    public ResponseEntity<Page<VideoPreviewDto>> findAllByUploaderId(@PathVariable UUID memberId, Pageable pageable) {
        Page<Video> videos = videoService.findAllPublicByUploaderId(memberId, pageable);
        return ResponseEntity.ok(videos.map(VideoPreviewDto::of));
    }

}
