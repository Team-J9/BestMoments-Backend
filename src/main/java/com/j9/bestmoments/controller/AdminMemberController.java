package com.j9.bestmoments.controller;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.dto.response.MemberFindDto;
import com.j9.bestmoments.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Admin-Member", description = "관리자 전용 사용자 관련 API")
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;

    @Operation(summary = "사용자 목록 조회")
    @GetMapping()
    public ResponseEntity<Page<MemberFindDto>> findAll(Pageable pageable) {
        Page<Member> members = memberService.findAll(pageable);
        return ResponseEntity.ok(members.map(MemberFindDto::of));
    }
    
}
