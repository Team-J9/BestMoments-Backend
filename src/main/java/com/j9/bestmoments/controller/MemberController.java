package com.j9.bestmoments.controller;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.dto.request.MemberUpdateDto;
import com.j9.bestmoments.dto.response.MemberFindDto;
import com.j9.bestmoments.dto.response.MemberPreviewDto;
import com.j9.bestmoments.service.MemberService;
import com.j9.bestmoments.util.AuthenticationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<MemberPreviewDto> getCurrentMemberSummary() {
        UUID memberId = AuthenticationUtil.getMemberId();
        Member member = memberService.findById(memberId);
        return ResponseEntity.ok(MemberPreviewDto.of(member));
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "사용자 상세 정보 조회", description = "사용자 id로 상세 정보 조회, 탈퇴한 사용자는 표기되지 않음")
    public ResponseEntity<MemberFindDto> getMemberInfo(@PathVariable UUID memberId) {
        Member member = memberService.findByIdAndIsDeletedFalse(memberId);
        return ResponseEntity.ok(MemberFindDto.of(member));
    }

    @DeleteMapping()
    @Operation(summary = "회원 탈퇴", description = "soft delete")
    public ResponseEntity<String> delete() {
        UUID memberId = AuthenticationUtil.getMemberId();
        Member member = memberService.findById(memberId);
        memberService.softDelete(member);
        return ResponseEntity.ok("성공적으로 탈퇴하였습니다.");
    }

    @PatchMapping("/restore")
    @Operation(summary = "회원 탈퇴 철회")
    public ResponseEntity<String> restore() {
        UUID memberId = AuthenticationUtil.getMemberId();
        Member member = memberService.findById(memberId);
        memberService.restore(member);
        return ResponseEntity.ok("성공적으로 복구하였습니다.");
    }

    @PatchMapping(path = "/my-page", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "현재 사용자 정보 수정")
    public ResponseEntity<MemberFindDto> update(@ModelAttribute @Valid MemberUpdateDto updateDto) {
        UUID memberId = AuthenticationUtil.getMemberId();
        Member member = memberService.findById(memberId);
        memberService.update(member, updateDto);
        return ResponseEntity.ok(MemberFindDto.of(member));
    }

}
