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
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
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

    @Test
    @Transactional
    void findById_Success() {
        UUID id = publicVideo.getId();
        Video foundVideo = videoService.findById(id);
        Assertions.assertEquals(publicVideo, foundVideo);
    }

    @Test
    @Transactional
    void findById_Fail() {
        UUID id = UUID.randomUUID();
        Assertions.assertThrows(EntityNotFoundException.class, () -> videoService.findById(id));
    }

    @Test
    @Transactional
    void findByIdAndUploaderId_Success() {
        UUID videoId = publicVideo.getId();
        UUID memberId = member.getId();
        Video foundVideo = videoService.findByIdAndUploaderId(videoId, memberId);
        Assertions.assertEquals(publicVideo, foundVideo);
    }

    @Test
    @Transactional
    void findByIdAndUploaderId_Fail() {
        UUID videoId = publicVideo.getId();
        UUID memberId = member.getId();
        UUID randomId = UUID.randomUUID();
        Assertions.assertThrows(EntityNotFoundException.class, () -> videoService.findByIdAndUploaderId(videoId, randomId));
        Assertions.assertThrows(EntityNotFoundException.class, () -> videoService.findByIdAndUploaderId(randomId, memberId));
    }

    @Test
    @Transactional
    void findDeletedByIdAndUploaderId_Success() {
        UUID videoId = publicVideo.getId();
        UUID memberId = member.getId();
        publicVideo.softDelete();
        videoRepository.save(publicVideo);
        Video foundVideo = videoService.findDeletedByIdAndUploaderId(videoId, memberId);
        Assertions.assertEquals(publicVideo, foundVideo);
    }

    @Test
    @Transactional
    void findDeletedByIdAndUploaderId_Fail() {
        UUID videoId = publicVideo.getId();
        UUID memberId = member.getId();
        Assertions.assertThrows(EntityNotFoundException.class, () -> videoService.findDeletedByIdAndUploaderId(videoId, memberId));
    }

}
