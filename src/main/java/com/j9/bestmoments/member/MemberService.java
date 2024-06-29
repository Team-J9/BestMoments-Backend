package com.j9.bestmoments.member;

import com.j9.bestmoments.auth.oauth.dto.request.OAuthUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member findOrSaveByOAuthInfo(OAuthUserInfoDto oAuth2UserInfo) {
        Member member = memberRepository.findByOauthProviderAndOauthId(oAuth2UserInfo.provider(), oAuth2UserInfo.id())
                .orElse(this.create(oAuth2UserInfo));
        return memberRepository.save(member);
    }

    private Member create(OAuthUserInfoDto oAuthUserInfoDto) {
        Member member = Member.builder()
                .oauthId(oAuthUserInfoDto.id())
                .oauthProvider(oAuthUserInfoDto.provider())
                .name(oAuthUserInfoDto.name())
                .email(oAuthUserInfoDto.email())
                .profileImageUrl(oAuthUserInfoDto.profileImageUrl())
                .role(MemberRole.USER)
                .build();

        return memberRepository.save(member);
    }

}
