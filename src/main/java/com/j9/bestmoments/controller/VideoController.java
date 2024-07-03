package com.j9.bestmoments.controller;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.dto.request.VideoCreateDto;
import com.j9.bestmoments.dto.response.VideoFindDto;
import com.j9.bestmoments.service.MemberService;
import com.j9.bestmoments.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController {

    private final VideoService videoService;
    private final MemberService memberService;

    @Operation(summary = "비디오 업로드")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoFindDto> upload(@ModelAttribute @Valid VideoCreateDto createDto) {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member member = memberService.findById(id);
        Video video = videoService.upload(member, createDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(VideoFindDto.of(video));
    }

}
