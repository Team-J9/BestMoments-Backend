package com.j9.bestmoments.controller;

import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.dto.response.VideoFindDto;
import com.j9.bestmoments.dto.response.VideoPreviewMoreDto;
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
@RequestMapping("/admin/videos")
@Tag(name = "Admin-Video", description = "관리자 전용 동영상 관련 API")
public class AdminVideoController {

    private final VideoService videoService;

    @Operation(summary = "모든 동영상 목록 조회", description = "비공개, 삭제된 영상을 포함")
    @GetMapping()
    public ResponseEntity<Page<VideoPreviewMoreDto>> findAll(Pageable pageable) {
        Page<Video> videos = videoService.findAll(pageable);
        return ResponseEntity.ok(videos.map(VideoPreviewMoreDto::of));
    }

    @Operation(summary = "동영상 열람", description = "비공개, 삭제된 영상 접근 가능")
    @GetMapping("/{videoId}")
    public ResponseEntity<VideoFindDto> findById(@PathVariable UUID videoId) {
        Video video = videoService.findById(videoId);
        return ResponseEntity.ok(VideoFindDto.of(video));
    }

}
