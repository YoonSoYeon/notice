package com.project.notice.unit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.project.notice.model.NoticeFile;
import com.project.notice.model.NoticeInfo;
import com.project.notice.model.dto.NoticeInfoDto;
import com.project.notice.model.dto.PageDto;
import com.project.notice.model.request.NoticeInfoCreationRequest;
import com.project.notice.repository.NoticeInfoRepository;
import com.project.notice.service.NoticeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class NoticeServiceTest {

	@InjectMocks
	private NoticeServiceImpl noticeService;

	@Mock
	private NoticeInfoRepository noticeInfoRepository;

	@Test
	@DisplayName("공지사항 전체 조회")
	public void readNoticeInfos() {
		// given
		NoticeInfo noticeInfo = NoticeInfo.builder().contents("test").title("test").writer("test").build();
		List<NoticeInfo> noticeInfos = List.of(noticeInfo);

		Pageable pageable = PageRequest.of(1, 1, Sort.by("noticeNo").ascending());
		PageImpl<NoticeInfo> page = new PageImpl<NoticeInfo>(noticeInfos);

		doReturn(page).when(noticeInfoRepository).findAll(pageable);

		// when
		PageDto findNoticeInfo = noticeService.readNoticeInfos(1, 1);

		// then
		assertEquals(noticeInfo.getTitle(), findNoticeInfo.getContent().get(0).getTitle());
	}

	@Test
	@DisplayName("공지사항 조회")
	public void readNoticeInfo() throws Exception {
		Long noticeNo = 1L;
		Long viewCount = 0L;

		NoticeInfo noticeInfo = NoticeInfo.builder().noticeNo(noticeNo).contents("test").title("test").writer("test").startDate(Timestamp.valueOf("2024-07-23 00:00:00")).endDate(Timestamp.valueOf("2024-07-23 23:59:59"))
				.viewCount(viewCount).build();

		doReturn(Optional.of(noticeInfo)).when(noticeInfoRepository).findById(noticeNo);

		NoticeInfoDto noticeInfoDto = noticeService.readNoticeInfo(noticeNo);

		assertEquals(noticeInfo.getTitle(), noticeInfoDto.getTitle());
	}

	@Test
	@DisplayName("공지사항 생성")
	public void createNoticeInfo() throws Exception {
		NoticeInfoCreationRequest request = NoticeInfoCreationRequest.builder().contents("test").title("test").writer("test").startDate(Timestamp.valueOf("2024-07-23 00:00:00")).endDate(Timestamp.valueOf("2024-07-23 23:59:59"))
				.build();

		NoticeInfo noticeInfo = new NoticeInfo();
		BeanUtils.copyProperties(request, noticeInfo);
		
		doReturn(noticeInfo).when(noticeInfoRepository).save(noticeInfo);
		
		assertAll(() -> noticeService.createNoticeInfo(request, null));
	}

	@Test
	@DisplayName("공지사항 전체 삭제")
	public void deleteNoticeInfo() throws Exception {
		doNothing().when(noticeInfoRepository).deleteAll();
		
		assertAll(() -> noticeService.deleteNoticeInfo(null));
	}

	@Test
	@DisplayName("공지사항 삭제")
	public void deleteNoticeInfoByNoticeNo() throws Exception {
		Long noticeNo = 1L;
		doNothing().when(noticeInfoRepository).deleteById(noticeNo);
		
		assertAll(() -> noticeService.deleteNoticeInfo(noticeNo));
	}

	@Test
	@DisplayName("공지사항 수정")
	public void updateNoticeInfo() throws Exception {
		Long noticeNo = 1L;
		NoticeInfo noticeInfo = NoticeInfo.builder().noticeNo(noticeNo).contents("test").title("test").writer("test").startDate(Timestamp.valueOf("2024-07-23 00:00:00")).endDate(Timestamp.valueOf("2024-07-23 23:59:59"))
				.Noticefiles(new ArrayList<NoticeFile>()).build();

		NoticeInfoCreationRequest request = new NoticeInfoCreationRequest();

		doReturn(Optional.of(noticeInfo)).when(noticeInfoRepository).findById(noticeNo);
		doReturn(noticeInfo).when(noticeInfoRepository).save(noticeInfo);

		BeanUtils.copyProperties(noticeInfo, request);

		assertAll(() -> noticeService.updateNoticeInfo(request, null, noticeNo));
	}
}