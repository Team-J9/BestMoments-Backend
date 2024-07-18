package com.j9.bestmoments.service;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.repository.MemberRepository;
import com.j9.bestmoments.util.MemberGenerator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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

    @Test
    void findAll() {
        Member member1 = MemberGenerator.createGoogleUser("user1");
        Member member2 = MemberGenerator.createGoogleUser("user2");
        Member member3 = MemberGenerator.createGoogleUser("user3");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> foundMembers = memberService
                .findAll(PageRequest.of(0, 100))
                .toList();

        Assertions.assertTrue(foundMembers.contains(member1));
        Assertions.assertTrue(foundMembers.contains(member2));
        Assertions.assertTrue(foundMembers.contains(member3));
    }


}
