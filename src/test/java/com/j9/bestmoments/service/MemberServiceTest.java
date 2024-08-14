package com.j9.bestmoments.service;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.MemberRole;
import com.j9.bestmoments.dto.request.MemberUpdateDto;
import com.j9.bestmoments.dto.response.OAuthUserInfoDto;
import com.j9.bestmoments.repository.MemberRepository;
import com.j9.bestmoments.util.MemberGenerator;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    private final Member member1 = MemberGenerator.createGoogleMember("user1", MemberRole.USER);
    private final Member member2 = MemberGenerator.createGoogleMember("user2", MemberRole.USER);
    private final Member member3 = MemberGenerator.createGoogleMember("user3", MemberRole.USER);

    @BeforeEach
    void createMembers() {
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
    }

    @Test
    void findAll() {
        List<Member> foundMembers = memberService
                .findAll(PageRequest.of(0, 100))
                .toList();
        Assertions.assertTrue(foundMembers.contains(member1));
        Assertions.assertTrue(foundMembers.contains(member2));
        Assertions.assertTrue(foundMembers.contains(member3));
    }

    @Test
    void findById_Success() {
        UUID id = member1.getId();
        Member foundMember = memberService.findById(id);
        Assertions.assertEquals(member1, foundMember);
    }

    @Test
    void findById_Fail() {
        UUID id = UUID.randomUUID();
        Assertions.assertThrows(EntityNotFoundException.class, () -> memberService.findById(id));
    }

    @Test
    void findByIdAndDeletedAtIsNull_Success() {
        UUID id = member1.getId();
        Member foundMember = memberService.findByIdAndDeletedAtIsNull(id);
        Assertions.assertEquals(member1, foundMember);
    }

    @Test
    void findByIdAndDeletedAtIsNull_Fail() {
        member1.softDelete();
        memberRepository.save(member1);
        UUID id = member1.getId();
        Assertions.assertThrows(EntityNotFoundException.class, () -> memberService.findByIdAndDeletedAtIsNull(id));
    }

    @Test
    void findOrSaveByOAuthInfo_Find() {
        OAuthUserInfoDto dto = new OAuthUserInfoDto(
                member1.getOauthProvider(),
                member1.getOauthId(),
                null,
                null,
                null
        );
        Page<Member> previousMembers = memberService.findAll(PageRequest.of(0, 100));

        memberService.findOrSaveByOAuthInfo(dto);
        Page<Member> currentMembers = memberService.findAll(PageRequest.of(0, 100));

        long previousMembersCount = previousMembers.stream().count();
        long currentMembersCount = currentMembers.stream().count();
        Assertions.assertEquals(previousMembersCount, currentMembersCount);
    }

    @Test
    void findOrSaveByOAuthInfo_Save() {
        OAuthUserInfoDto dto = new OAuthUserInfoDto(
                member1.getOauthProvider(),
                UUID.randomUUID().toString(),
                null,
                null,
                null
        );
        Page<Member> previousMembers = memberService.findAll(PageRequest.of(0, 100));

        memberService.findOrSaveByOAuthInfo(dto);
        Page<Member> currentMembers = memberService.findAll(PageRequest.of(0, 100));

        long previousMembersCount = previousMembers.stream().count();
        long currentMembersCount = currentMembers.stream().count();
        Assertions.assertEquals(previousMembersCount + 1, currentMembersCount);
    }

    @Test
    void update() {
        String changedName = "changedName";
        String changedDescription = "changedDescription";
        MemberUpdateDto dto = new MemberUpdateDto(null, changedName, changedDescription);

        memberService.update(member1, dto);

        Member foundMember = memberService.findById(member1.getId());
        Assertions.assertEquals(changedName, foundMember.getName());
        Assertions.assertEquals(changedDescription, foundMember.getDescription());
    }

    @Test
    void softDelete() {
        memberService.softDelete(member1);
        UUID id = member1.getId();
        Assertions.assertThrows(EntityNotFoundException.class, () -> memberService.findByIdAndDeletedAtIsNull(id));
    }

    @Test
    void restore() {
        memberService.softDelete(member1);
        memberService.restore(member1);
        UUID id = member1.getId();
        Assertions.assertDoesNotThrow(() -> memberService.findByIdAndDeletedAtIsNull(id));
    }

}
