package com.j9.bestmoments.auth;

import com.j9.bestmoments.auth.oauth.dto.request.OAuthUserInfoDto;
import com.j9.bestmoments.auth.oauth.service.GoogleAuthService;
import com.j9.bestmoments.auth.oauth.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/login/{oAuthProvider}")
    @Operation(summary = "OAuth 인증코드로 로그인/회원가입", description = "oAuthProvider: google")
    public String login(@PathVariable String oAuthProvider, @RequestParam String code) {
        OAuthService oAuthService = switch (oAuthProvider) {
            case "google" -> googleAuthService;
            default -> throw new OAuth2AuthenticationException("존재하지 않는 OAuth 인증 방식입니다.");
        };
        OAuthUserInfoDto oAuthUserInfo = oAuthService.getUserInfo(code);
        log.info(oAuthUserInfo.toString());
        return "토큰 발급";
    }

}
