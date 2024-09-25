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

    Page<Video> findAllByUploaderIdAndIsDeletedTrue(UUID uploaderId, PageRequest pageRequest);

    Page<Video> findAllByUploaderIdAndIsDeletedFalse(UUID uploaderId, PageRequest pageRequest);

    Page<Video> findAllByUploaderIdAndVideoStatusAndIsDeletedFalse(UUID uploaderId, VideoStatus videoStatus, PageRequest pageRequest);

    Optional<Video> findByIdAndUploaderIdAndIsDeletedTrue(UUID id, UUID uploaderId);

    Optional<Video> findByIdAndVideoStatusAndIsDeletedFalse(UUID id, VideoStatus videoStatus);

    Optional<Video> findByIdAndUploaderId(UUID id, UUID uploaderId);

    Page<Video> findAllByTagsContainsAndVideoStatusAndIsDeletedFalse(String tag, VideoStatus videoStatus, PageRequest pageRequest);

    Page<Video> findAllByUploaderIdAndTagsContainsAndIsDeletedFalse(UUID uploaderId, String tag, PageRequest pageRequest);

}
