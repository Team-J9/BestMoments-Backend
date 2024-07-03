package com.j9.bestmoments.service;

import com.j9.bestmoments.dto.response.OAuthUserInfoDto;
import com.j9.bestmoments.domain.MemberRole;
import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findById(String id) {
        return memberRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
    }

    public Member findById(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
    }

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
