package com.j9.bestmoments.repository;

import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.domain.VideoStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {

    Page<Video> findAllByUploaderIdAndDeletedAtIsNotNull(UUID uploaderId, PageRequest pageRequest);

    Page<Video> findAllByUploaderIdAndDeletedAtIsNull(UUID uploaderId, PageRequest pageRequest);

    Page<Video> findAllByUploaderIdAndVideoStatusAndDeletedAtIsNull(UUID uploaderId, VideoStatus videoStatus, PageRequest pageRequest);

    Optional<Video> findByIdAndUploaderIdAndDeletedAtIsNotNull(UUID id, UUID uploaderId);

    Optional<Video> findByIdAndVideoStatusAndDeletedAtIsNull(UUID id, VideoStatus videoStatus);

    Optional<Video> findByIdAndUploaderId(UUID id, UUID uploaderId);

    Page<Video> findAllByTagsContainsAndVideoStatusAndDeletedAtIsNull(String tag, VideoStatus videoStatus, PageRequest pageRequest);

    Page<Video> findAllByUploaderIdAndTagsContainsAndDeletedAtIsNull(UUID uploaderId, String tag, PageRequest pageRequest);

}
