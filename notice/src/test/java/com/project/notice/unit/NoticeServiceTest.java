package com.project.notice.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

import com.project.notice.exception.ExistException;
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
		//given
		Integer pageNo = 0;
		Integer pageSize = 1;
		
		NoticeInfo noticeInfo = NoticeInfo.builder().contents("test").title("test").writer("test").build();
		List<NoticeInfo> noticeInfos = List.of(noticeInfo);

		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("noticeNo").ascending());
		PageImpl<NoticeInfo> page = new PageImpl<NoticeInfo>(noticeInfos);

		doReturn(page).when(noticeInfoRepository).findAll(pageable);

		//when
		PageDto findNoticeInfo = noticeService.readNoticeInfos(pageNo, pageSize);

		//then
		assertEquals(noticeInfo.getTitle(), findNoticeInfo.getContent().get(0).getTitle());
	}

	@Test
	@DisplayName("공지사항 조회")
	public void readNoticeInfo() throws Exception {
		//given
		Long noticeNo = 1L;
		Long viewCount = 0L;

		NoticeInfo noticeInfo = NoticeInfo.builder().noticeNo(noticeNo).contents("test").title("test").writer("test").startDate(Timestamp.valueOf("2024-07-23 00:00:00")).endDate(Timestamp.valueOf("2024-07-23 23:59:59"))
				.viewCount(viewCount).build();

		doReturn(Optional.of(noticeInfo)).when(noticeInfoRepository).findById(noticeNo);

		//when
		NoticeInfoDto noticeInfoDto = noticeService.readNoticeInfo(noticeNo);

		//then
		assertEquals(noticeInfo.getTitle(), noticeInfoDto.getTitle());
	}

	@Test
	@DisplayName("공지사항 생성")
	public void createNoticeInfo() throws Exception {
		//given
		NoticeInfoCreationRequest request = NoticeInfoCreationRequest.builder().contents("test").title("test").writer("test").startDate(Timestamp.valueOf("2024-07-23 00:00:00")).endDate(Timestamp.valueOf("2024-07-23 23:59:59"))
				.build();

		NoticeInfo noticeInfo = new NoticeInfo();
		BeanUtils.copyProperties(request, noticeInfo);

		doReturn(noticeInfo).when(noticeInfoRepository).save(noticeInfo);

		//when, then
		assertAll(() -> noticeService.createNoticeInfo(request, null));
	}
	
	@Test
	@DisplayName("공지사항 생성 - 제목 중복 체크")
	public void createNoticeInfo_existTitle() throws Exception {
		//given
		NoticeInfoCreationRequest request = NoticeInfoCreationRequest.builder().contents("test").title("test").writer("test").startDate(Timestamp.valueOf("2024-07-23 00:00:00")).endDate(Timestamp.valueOf("2024-07-23 23:59:59"))
				.build();
		NoticeInfo noticeInfo = new NoticeInfo();
		BeanUtils.copyProperties(request, noticeInfo);
		
		doReturn(Optional.of(noticeInfo)).when(noticeInfoRepository).findByTitle("test");
		
		//when, then
		assertThatThrownBy(() -> noticeService.createNoticeInfo(request, null)).isInstanceOf(ExistException.class);
	}

	@Test
	@DisplayName("공지사항 삭제")
	public void deleteNoticeInfoByNoticeNo() throws Exception {
		//given
		Long noticeNo = 1L;
		doNothing().when(noticeInfoRepository).deleteById(noticeNo);

		//when, then
		assertAll(() -> noticeService.deleteNoticeInfo(noticeNo));
	}

	@Test
	@DisplayName("공지사항 수정")
	public void updateNoticeInfo() throws Exception {
		//given
		Long noticeNo = 1L;
		NoticeInfo noticeInfo = NoticeInfo.builder().noticeNo(noticeNo).contents("test").title("test").writer("test").startDate(Timestamp.valueOf("2024-07-23 00:00:00")).endDate(Timestamp.valueOf("2024-07-23 23:59:59"))
				.Noticefiles(new ArrayList<NoticeFile>()).build();

		NoticeInfoCreationRequest request = new NoticeInfoCreationRequest();

		doReturn(Optional.of(noticeInfo)).when(noticeInfoRepository).findById(noticeNo);
		doReturn(noticeInfo).when(noticeInfoRepository).save(noticeInfo);

		BeanUtils.copyProperties(noticeInfo, request);

		//when, then
		assertAll(() -> noticeService.updateNoticeInfo(request, null, noticeNo));
	}
}
