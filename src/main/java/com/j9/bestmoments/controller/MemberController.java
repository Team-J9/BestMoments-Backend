package com.j9.bestmoments.controller;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.dto.response.MemberSummaryDto;
import com.j9.bestmoments.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Member", description = "사용자 관련 API")
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/my-page")
    @Operation(summary = "현재 사용자 정보 조회", description = "현재 사용자 헤더 정보 조회")
    public ResponseEntity<MemberSummaryDto> getCurrentMemberSummary() {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member member = memberService.findById(memberId);
        return ResponseEntity.ok(MemberSummaryDto.of(member));
    }

}
