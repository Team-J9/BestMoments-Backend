package com.j9.bestmoments.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
public class Video {

    @Id
    private UUID id;

    private String videoUrl;
    private String thumbnailUrl;
    @Lob
    private List<String> encodedVideoUrls;

    private String title;
    @Lob
    private String description;
    @Enumerated(EnumType.STRING)
    private VideoStatus videoStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Cascade(CascadeType.ALL)
    private Member uploader;

    @CreatedDate
    private LocalDateTime createdAt;
    private boolean isDeleted;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    @Builder
    public Video(Member uploader, String title, String description, VideoStatus videoStatus) {
        this.id = UUID.randomUUID();
        this.uploader = uploader;
        this.title = title;
        this.description = description;
        this.videoStatus = videoStatus;
        this.isDeleted = false;
        this.encodedVideoUrls = new ArrayList<>();
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public void restore() {
        this.isDeleted = false;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVideoStatus(VideoStatus videoStatus) {
        this.videoStatus = videoStatus;
    }

    public void setVideoTags(List<String> tags) {
        this.tags = tags;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void addEncodedVideoUrl(String videoUrl) {
        this.encodedVideoUrls.add(videoUrl);
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

}
