package com.j9.bestmoments.controller;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.domain.VideoStatus;
import com.j9.bestmoments.dto.request.VideoCreateDto;
import com.j9.bestmoments.dto.response.VideoFindDto;
import com.j9.bestmoments.dto.response.VideoPreviewDto;
import com.j9.bestmoments.service.MemberService;
import com.j9.bestmoments.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController {

    private final VideoService videoService;
    private final MemberService memberService;

    @Operation(summary = "동영상 업로드")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoFindDto> upload(@ModelAttribute @Valid VideoCreateDto createDto) {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member member = memberService.findById(id);
        Video video = videoService.upload(member, createDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(VideoFindDto.of(video));
    }

    @Operation(summary = "동영상 열람")
    @GetMapping("/{videoId}")
    public ResponseEntity<VideoFindDto> findById(@PathVariable UUID videoId) {
        Video video = videoService.findById(videoId);
        return ResponseEntity.ok(VideoFindDto.of(video));
    }

    @Operation(summary = "사용자의 동영상 목록 조회")
    @GetMapping("/by/{memberId}")
    public ResponseEntity<Page<VideoPreviewDto>> findAllByUploaderId(@PathVariable UUID memberId, Pageable pageable) {
        UUID currentMemberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ResponseEntity.ok(
                videoService.findAllByUploaderId(currentMemberId, memberId, pageable)
                        .map(VideoPreviewDto::of)
        );
    }

}
