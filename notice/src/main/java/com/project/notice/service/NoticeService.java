package com.project.notice.service;

import org.springframework.web.multipart.MultipartFile;

import com.project.notice.model.dto.NoticeInfoDto;
import com.project.notice.model.dto.PageDto;
import com.project.notice.model.request.NoticeInfoCreationRequest;
import com.project.notice.model.request.NoticeInfoUpdationRequest;

public interface NoticeService {
	public PageDto readNoticeInfos(int pageNo, int pageSize);

	public NoticeInfoDto readNoticeInfo(Long noticeNo) throws Exception;

	public void createNoticeInfo(NoticeInfoCreationRequest noticeInfo, MultipartFile[] files) throws Exception;

	public void deleteNoticeInfo(Long noticeNo);

	public void updateNoticeInfo(NoticeInfoUpdationRequest request, MultipartFile[] files, Long noticeNo) throws Exception;
}
