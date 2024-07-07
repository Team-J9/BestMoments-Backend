package com.j9.bestmoments.controller;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.dto.request.MemberUpdateDto;
import com.j9.bestmoments.dto.response.MemberFindDto;
import com.j9.bestmoments.dto.response.MemberSummaryDto;
import com.j9.bestmoments.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/{memberId}")
    @Operation(summary = "사용자 상세 정보 조회", description = "사용자 id로 상세 정보 조회, 탈퇴한 사용자는 표기되지 않음")
    public ResponseEntity<MemberFindDto> getMemberInfo(@PathVariable String memberId) {
        Member member = memberService.findByIdAndDeletedAtIsNull(UUID.fromString(memberId));
        return ResponseEntity.ok(MemberFindDto.of(member));
    }

    @DeleteMapping()
    @Operation(summary = "회원 탈퇴", description = "soft delete")
    public ResponseEntity<String> delete() {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member member = memberService.findById(memberId);
        memberService.softDelete(member);
        return ResponseEntity.ok("성공적으로 탈퇴하였습니다.");
    }

    @PatchMapping("/restore")
    @Operation(summary = "회원 탈퇴 철회")
    public ResponseEntity<String> restore() {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member member = memberService.findById(memberId);
        memberService.restore(member);
        return ResponseEntity.ok("성공적으로 복구하였습니다.");
    }

    @PatchMapping("/my-page")
    @Operation(summary = "현재 사용자 정보 수정")
    public ResponseEntity<MemberFindDto> update(@RequestBody MemberUpdateDto updateDto) {
        UUID memberId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Member member = memberService.findById(memberId);
        memberService.update(member, updateDto);
        return ResponseEntity.ok(MemberFindDto.of(member));
    }

}
