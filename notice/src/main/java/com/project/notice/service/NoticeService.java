package com.project.notice.service;

import org.springframework.web.multipart.MultipartFile;

import com.project.notice.model.dto.NoticeInfoDto;
import com.project.notice.model.dto.PageDto;
import com.project.notice.model.request.NoticeInfoCreationRequest;

public interface NoticeService {
	public PageDto readNoticeInfos(int pageNo, int pageSize);
	public NoticeInfoDto readNoticeInfo(Long noticeNo);
	public void createNoticeInfo(NoticeInfoCreationRequest noticeInfo, MultipartFile[] files) throws Exception;
	public void deleteNoticeInfo(Long noticeNo);
	public void updateNoticeInfo(NoticeInfoCreationRequest request, MultipartFile[] files, Long noticeNo) throws Exception;
}
