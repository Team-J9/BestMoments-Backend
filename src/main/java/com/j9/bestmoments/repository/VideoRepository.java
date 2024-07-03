package com.j9.bestmoments.repository;

import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.domain.VideoStatus;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {

    Page<Video> findAllByUploaderIdAndVideoStatusAndDeletedAtIsNull(UUID uploaderId, VideoStatus videoStatus, PageRequest pageRequest);

    Page<Video> findAllByUploaderIdAndDeletedAtIsNull(UUID uploaderId, PageRequest pageRequest);

}
