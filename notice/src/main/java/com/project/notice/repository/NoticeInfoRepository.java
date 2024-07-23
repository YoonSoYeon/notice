package com.project.notice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.notice.model.NoticeInfo;

public interface NoticeInfoRepository extends JpaRepository<NoticeInfo, Long> {

	@Modifying
	@Query("update NoticeInfo n set n.viewCount = n.viewCount + 1 where n.noticeNo = :noticeNo")
	void updateViewCount(@Param("noticeNo") Long noticeNo);
	
	Optional<NoticeInfo> findByTitle(String title);
}
