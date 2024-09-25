package com.j9.bestmoments.util;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.domain.VideoStatus;

public final class VideoGenerator {

    public static Video createVideo(Member member, String title, VideoStatus videoStatus) {
        Video video = Video.builder()
                .uploader(member)
                .title(title)
                .description(title + "'s description")
                .videoStatus(videoStatus)
                .build();
        video.setVideoUrl(title + "'s fileUrl");
        return video;
    }

}
