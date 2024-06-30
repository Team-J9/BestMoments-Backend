package com.j9.bestmoments.controller;

import com.j9.bestmoments.dto.response.JwtTokenDto;
import com.j9.bestmoments.jwt.JwtTokenProvider;
import com.j9.bestmoments.dto.response.OAuthUserInfoDto;
import com.j9.bestmoments.service.GoogleAuthService;
import com.j9.bestmoments.service.OAuthService;
import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final GoogleAuthService googleAuthService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login/{oAuthProvider}")
    @Operation(summary = "OAuth 인증코드로 로그인/회원가입", description = "oAuthProvider: google")
    public ResponseEntity<JwtTokenDto> login(@PathVariable String oAuthProvider, @RequestParam String code) {
        OAuthService oAuthService = switch (oAuthProvider) {
            case "google" -> googleAuthService;
            default -> throw new OAuth2AuthenticationException("존재하지 않는 OAuth 인증 방식입니다.");
        };
        OAuthUserInfoDto oAuthUserInfo = oAuthService.getUserInfo(code);
        Member member = memberService.findOrSaveByOAuthInfo(oAuthUserInfo);

        JwtTokenDto jwtToken = jwtTokenProvider.generateToken(member);
        return ResponseEntity.ok(jwtToken);
    }

}
