package com.project.notice.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.project.notice.code.ExceptionCode;
import com.project.notice.code.MessageCode;
import com.project.notice.exception.NoticeNotFoundException;
import com.project.notice.model.NoticeInfo;
import com.project.notice.model.dto.NoticeInfoDto;
import com.project.notice.repository.NoticeInfoRepository;
import com.project.notice.service.NoticeServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NoticeControllerTest {

	@Autowired
	private NoticeServiceImpl noticeService;

	@Autowired
	private NoticeInfoRepository noticeRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("공지사항 조회")
	public void readNoticeInfo() throws Exception {
		NoticeInfo noticeInfo = NoticeInfo.builder().title("test").contents("test").writer("test").build();

		NoticeInfo info = noticeRepository.save(noticeInfo);

		ResultActions resultActions = mockMvc.perform(get("/api/notice/info/{noticeNo}", info.getNoticeNo()));

		resultActions.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.result.data.title", is(noticeInfo.getTitle())));
	}

	@Test
	@DisplayName("공지사항 조회 - 조회 수 증가 테스트")
	public void readNoticeInfo_increaseViewCount() throws Exception {
		NoticeInfo noticeInfo = NoticeInfo.builder().title("test").contents("test").writer("test").build();

		NoticeInfo info = noticeRepository.save(noticeInfo);

		ResultActions resultActions = mockMvc.perform(get("/api/notice/info/{noticeNo}", info.getNoticeNo()));

		resultActions.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.result.data.viewCount", is(1)));
	}
	
	@Test
	@DisplayName("존재하지 않는 공지사항 조회")
	public void readNoticeInfo_fail() throws Exception {
		NoticeInfo noticeInfo = NoticeInfo.builder().title("test").contents("test").writer("test").build();

		NoticeInfo info = noticeRepository.save(noticeInfo);

		ResultActions resultActions = mockMvc.perform(get("/api/notice/info/{noticeNo}", info.getNoticeNo()+1));
		
		resultActions.andExpect(status().isNotFound()).andDo(print()).andExpect(jsonPath("$.result.msg", is(ExceptionCode.NOTICE_NOT_FOUND.getMessage())));

	}

	@Test
	@DisplayName("공지사항 저장 성공")
	public void createNoticeInfo() throws Exception {
		ResultActions resultActions = mockMvc.perform(multipart("/api/notice/info").param("title", "test11").param("contents", "test").param("writer", "test").param("startDate", "2024-07-24 00:00:00")
				.param("endDate", "2024-07-24 23:59:59").contentType(MediaType.MULTIPART_FORM_DATA));

		resultActions.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.result.data", is(MessageCode.NOTICE_SAVE_SUCCESS.getMessage())));
	}

	@Test
	@DisplayName("공지사항 저장 - 유효성 검사(제목 필수누락)")
	public void createNoticeInfo_validationTitle() throws Exception {
		ResultActions resultActions = mockMvc.perform(
				multipart("/api/notice/info").param("contents", "test").param("writer", "test").param("startDate", "2024-07-24 00:00:00").param("endDate", "2024-07-24 23:59:59").contentType(MediaType.MULTIPART_FORM_DATA));

		resultActions.andExpect(status().isBadRequest()).andDo(print()).andExpect(jsonPath("$.result.msg", is("제목은 필수입력 사항입니다.")));
	}

	@Test
	@DisplayName("공지사항 저장 - 유효성 검사(작성자 최대 길이)")
	public void createNoticeInfo_validationWriter() throws Exception {
		ResultActions resultActions = mockMvc.perform(multipart("/api/notice/info").param("title", "test111").param("contents", "test").param("writer", "testtesttesttesttesttesttesttesttesttesttesttesttesttesttest").param("startDate", "2024-07-24 00:00:00")
				.param("endDate", "2024-07-24 23:59:59").contentType(MediaType.MULTIPART_FORM_DATA));

		resultActions.andExpect(status().isBadRequest()).andDo(print()).andExpect(jsonPath("$.result.msg", is("작성자의 최대 길이는 32자 입니다.")));
	}

	@Test
	@DisplayName("공지사항 저장 - 제목 중복 체크")
	public void createNoticeInfo_validationExistTitle() throws Exception {
		NoticeInfo noticeInfo = NoticeInfo.builder().title("test111").contents("test").writer("test").build();

		NoticeInfo info = noticeRepository.save(noticeInfo);

		ResultActions resultActions = mockMvc.perform(multipart("/api/notice/info").param("title", info.getTitle()).param("contents", info.getContents()).param("writer", info.getWriter()).param("startDate", "2024-07-24 00:00:00")
				.param("endDate", "2024-07-24 23:59:59").contentType(MediaType.MULTIPART_FORM_DATA));

		resultActions.andExpect(status().isInternalServerError()).andDo(print()).andExpect(jsonPath("$.result.msg", is(ExceptionCode.NOTICE_EXIST.getMessage())));
	}

	@Test
	@DisplayName("공지사항 삭제")
	public void deleteNoticeInfo() throws Exception {
		NoticeInfo noticeInfo = NoticeInfo.builder().title("test111").contents("test").writer("test").startDate(Timestamp.valueOf("2024-07-24 00:00:00")).endDate(Timestamp.valueOf("2024-07-24 23:59:59")).build();

		NoticeInfo info = noticeRepository.save(noticeInfo);

		ResultActions resultActions = mockMvc.perform(delete("/api/notice/info/{noticeNo}", info.getNoticeNo()).contentType(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.result.data", is(MessageCode.NOTICE_DELETE_SUCCESS.getMessage())));

		assertThatThrownBy(() -> noticeService.readNoticeInfo(info.getNoticeNo())).isInstanceOf(NoticeNotFoundException.class);
	}

	@Test
	@DisplayName("공지사항 수정 - 내용 수정")
	public void updateNoticeInfo_updateContent() throws Exception {
		NoticeInfo noticeInfo = NoticeInfo.builder().title("test111").contents("test").writer("test").startDate(Timestamp.valueOf("2024-07-24 00:00:00")).endDate(Timestamp.valueOf("2024-07-24 23:59:59")).build();

		NoticeInfo info = noticeRepository.save(noticeInfo);

		ResultActions resultActions = mockMvc.perform(multipart(HttpMethod.PATCH, "/api/notice/info/{noticeNo}", info.getNoticeNo()).param("contents", "changeContents").param("writer", info.getWriter())
				.param("startDate", info.getStartDate().toString()).param("endDate", info.getEndDate().toString()).contentType(MediaType.MULTIPART_FORM_DATA));

		resultActions.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.result.data", is(MessageCode.NOTICE_UPDATE_SUCCESS.getMessage())));

		NoticeInfoDto updatedNoticeInfo = noticeService.readNoticeInfo(info.getNoticeNo());
		assertThat(updatedNoticeInfo.getContents()).isEqualTo("changeContents");
	}
	
	@Test
	@DisplayName("공지사항 수정 - 제목 수정")
	public void updateNoticeInfo_updateTitle() throws Exception {
		NoticeInfo noticeInfo = NoticeInfo.builder().title("test111").contents("test").writer("test").startDate(Timestamp.valueOf("2024-07-24 00:00:00")).endDate(Timestamp.valueOf("2024-07-24 23:59:59")).build();
		
		NoticeInfo info = noticeRepository.save(noticeInfo);

		ResultActions resultActions = mockMvc.perform(multipart(HttpMethod.PATCH, "/api/notice/info/{noticeNo}", info.getNoticeNo()).param("title", "changeTitle").contentType(MediaType.MULTIPART_FORM_DATA));

		resultActions.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.result.data", is(MessageCode.NOTICE_UPDATE_SUCCESS.getMessage())));
		
		NoticeInfoDto updatedNoticeInfo = noticeService.readNoticeInfo(info.getNoticeNo());
		assertThat(updatedNoticeInfo.getTitle()).isEqualTo("changeTitle");
	}
	
	@Test
	@DisplayName("공지사항 수정 - 제목 중복 체크")
	public void updateNoticeInfo_validationExistTitle() throws Exception {
		NoticeInfo noticeInfo = NoticeInfo.builder().title("test111").contents("test").writer("test").startDate(Timestamp.valueOf("2024-07-24 00:00:00")).endDate(Timestamp.valueOf("2024-07-24 23:59:59")).build();
		NoticeInfo noticeInfo2 = NoticeInfo.builder().title("test222").contents("test").writer("test").startDate(Timestamp.valueOf("2024-07-24 00:00:00")).endDate(Timestamp.valueOf("2024-07-24 23:59:59")).build();
		
		NoticeInfo info = noticeRepository.save(noticeInfo);
		noticeRepository.save(noticeInfo2);

		ResultActions resultActions = mockMvc.perform(multipart(HttpMethod.PATCH, "/api/notice/info/{noticeNo}", info.getNoticeNo()).param("title", noticeInfo2.getTitle()).contentType(MediaType.MULTIPART_FORM_DATA));

		resultActions.andExpect(status().isInternalServerError()).andDo(print()).andExpect(jsonPath("$.result.msg", is(ExceptionCode.NOTICE_EXIST.getMessage())));
	}
}
