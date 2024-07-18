package com.j9.bestmoments.service;

import com.j9.bestmoments.domain.Member;
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
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    private final Member member1 = MemberGenerator.createGoogleUser("user1");
    private final Member member2 = MemberGenerator.createGoogleUser("user2");
    private final Member member3 = MemberGenerator.createGoogleUser("user3");

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


}
