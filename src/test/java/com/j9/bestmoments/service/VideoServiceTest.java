package com.j9.bestmoments.service;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.MemberRole;
import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.domain.VideoStatus;
import com.j9.bestmoments.dto.request.VideoCreateDto;
import com.j9.bestmoments.repository.MemberRepository;
import com.j9.bestmoments.repository.VideoRepository;
import com.j9.bestmoments.util.MemberGenerator;
import com.j9.bestmoments.util.VideoGenerator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class VideoServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private VideoService videoService;
    @Autowired
    private VideoRepository videoRepository;

    private Member member = MemberGenerator.createGoogleMember("user1", MemberRole.USER);
    private Video publicVideo = VideoGenerator.createVideo(member, "video1", VideoStatus.PUBLIC);
    private Video privateVideo = VideoGenerator.createVideo(member, "video2", VideoStatus.PRIVATE);
    private Video urlPublicVideo = VideoGenerator.createVideo(member, "video3", VideoStatus.URL_PUBLIC);

    @BeforeEach
    @Transactional
    void setup() {
        memberRepository.save(member);
        videoRepository.save(publicVideo);
        videoRepository.save(privateVideo);
        videoRepository.save(urlPublicVideo);
    }

    @Test
    @Transactional
    void findAll() {
        List<Video> foundVideos = videoService
                .findAll(PageRequest.of(0, 100))
                .toList();
        Assertions.assertTrue(foundVideos.contains(publicVideo));
        Assertions.assertTrue(foundVideos.contains(privateVideo));
        Assertions.assertTrue(foundVideos.contains(urlPublicVideo));
    }

}
