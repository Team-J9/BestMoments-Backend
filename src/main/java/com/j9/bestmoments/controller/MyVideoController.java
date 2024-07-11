package com.j9.bestmoments.controller;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.dto.request.VideoCreateDto;
import com.j9.bestmoments.dto.request.VideoUpdateDto;
import com.j9.bestmoments.dto.response.VideoFindDto;
import com.j9.bestmoments.dto.response.VideoPreviewDto;
import com.j9.bestmoments.service.MemberService;
import com.j9.bestmoments.service.VideoService;
import com.j9.bestmoments.util.AuthenticationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-videos")
@Tag(name = "My Video", description = "내 동영상 관련 API")
public class MyVideoController {

    private final VideoService videoService;
    private final MemberService memberService;

    @Operation(summary = "동영상 업로드")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoFindDto> upload(@ModelAttribute @Valid VideoCreateDto createDto) {
        UUID memberId = AuthenticationUtil.getMemberId();
        Member member = memberService.findById(memberId);
        Video video = videoService.upload(member, createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(VideoFindDto.of(video));
    }

    @Operation(summary = "내 동영상 목록 조회")
    @GetMapping()
    public ResponseEntity<Page<VideoPreviewDto>> findAll(Pageable pageable) {
        UUID memberId = AuthenticationUtil.getMemberId();
        Page<Video> videos = videoService.findAllActivatedByUploaderId(memberId, pageable);
        return ResponseEntity.ok(videos.map(VideoPreviewDto::of));
    }

    @Operation(summary = "내 동영상 열람")
    @GetMapping("/{videoId}")
    public ResponseEntity<VideoFindDto> findById(@PathVariable UUID videoId) {
        UUID memberId = AuthenticationUtil.getMemberId();
        Video video = videoService.findByIdAndUploaderId(videoId, memberId);
        return ResponseEntity.ok(VideoFindDto.of(video));
    }

    @Operation(summary = "내 동영상 정보 수정")
    @PatchMapping("/{videoId}")
    public ResponseEntity<VideoFindDto> update(@PathVariable UUID videoId, @RequestBody VideoUpdateDto updateDto) {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Video video = videoService.findByIdAndUploaderId(videoId, memberId);
        videoService.update(video, updateDto);
        return ResponseEntity.ok(VideoFindDto.of(video));
    }

    @Operation(summary = "내 동영상 삭제 (휴지통으로 이동)")
    @DeleteMapping("/{videoId}")
    public ResponseEntity<VideoFindDto> softDelete(@PathVariable UUID videoId) {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Video video = videoService.findByIdAndUploaderId(videoId, memberId);
        videoService.softDelete(video);
        return ResponseEntity.ok(VideoFindDto.of(video));
    }

    @Operation(summary = "내 동영상 복구 (휴지통에서 복구)")
    @PostMapping("/{videoId}")
    public ResponseEntity<VideoFindDto> restore(@PathVariable UUID videoId) {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Video video = videoService.findDeletedByIdAndUploaderId(videoId, memberId);
        videoService.restore(video);
        return ResponseEntity.ok(VideoFindDto.of(video));
    }

    @Operation(summary = "내 휴지통 조회")
    @GetMapping("/deleted")
    public ResponseEntity<Page<VideoPreviewDto>> findDeletedVideos(Pageable pageable) {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<Video> videos = videoService.findAllDeletedByUploaderId(memberId, pageable);
        return ResponseEntity.ok(videos.map(VideoPreviewDto::of));
    }

}
