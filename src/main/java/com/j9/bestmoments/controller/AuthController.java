package com.j9.bestmoments.controller;

import com.j9.bestmoments.dto.response.LoginDto;
import com.j9.bestmoments.dto.response.OAuthUserInfoDto;
import com.j9.bestmoments.service.GoogleAuthService;
import com.j9.bestmoments.service.OAuthService;
import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.service.MemberService;
import com.j9.bestmoments.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
@Slf4j
public class AuthController {

    private final GoogleAuthService googleAuthService;
    private final MemberService memberService;
    private final TokenService tokenService;

    @GetMapping("/login/{oAuthProvider}")
    @Operation(summary = "OAuth 인증코드로 로그인/회원가입", description = "oAuthProvider: google")
    public ResponseEntity<LoginDto> login(@PathVariable String oAuthProvider, @RequestParam String code) {
        OAuthService oAuthService = switch (oAuthProvider) {
            case "google" -> googleAuthService;
            default -> throw new OAuth2AuthenticationException("존재하지 않는 OAuth 인증 방식입니다.");
        };
        OAuthUserInfoDto oAuthUserInfo = oAuthService.getUserInfo(code);
        Member member = memberService.findOrSaveByOAuthInfo(oAuthUserInfo);

        LoginDto jwtToken = tokenService.create(member);
        return ResponseEntity.ok(jwtToken);
    }

    @PatchMapping("/logout")
    @Operation(summary = "로그아웃", description = "토큰을 강제로 만료시킵니다.")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        tokenService.expire(token);
        return ResponseEntity.ok().body("로그아웃에 성공하였습니다.");
    }

    @PatchMapping("/refresh")
    @Operation(summary = "액세스토큰 재발급", description = "리프래시 토큰을 통해 재발급받습니다.")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        String accessToken = tokenService.refresh(refreshToken);
        return ResponseEntity.ok().body(accessToken);
    }

}
