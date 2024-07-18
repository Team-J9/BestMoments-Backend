package com.j9.bestmoments.util;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.domain.VideoStatus;

public final class VideoGenerator {

    public static Video createVideo(Member member, String title, VideoStatus videoStatus) {
        return Video.builder()
                .uploader(member)
                .fileUrl(title + "'s fileUrl")
                .title(title)
                .description(title + "'s description")
                .videoStatus(videoStatus)
                .build();
    }

}
