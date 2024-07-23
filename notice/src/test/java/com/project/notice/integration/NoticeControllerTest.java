package com.project.notice.integration;

import static org.hamcrest.CoreMatchers.is;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.notice.model.NoticeInfo;
import com.project.notice.model.request.NoticeInfoCreationRequest;
import com.project.notice.repository.NoticeInfoRepository;
import com.project.notice.service.NoticeServiceImpl;

import jakarta.servlet.http.Part;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NoticeControllerTest{

	@Autowired
	private NoticeServiceImpl noticeService;
	
	@Autowired
	private NoticeInfoRepository noticeRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	@DisplayName("공지사항 조회")
	public void readNoticeInfo() throws Exception {
		NoticeInfo noticeInfo = NoticeInfo.builder()
				.title("test")
				.contents("test")
				.writer("test")
				.build();
		
		NoticeInfo info = noticeRepository.save(noticeInfo);
		
		ResultActions resultActions = mockMvc.perform(get("/api/notice/info/{noticeNo}", info.getNoticeNo()));
		
		resultActions.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.result.data.title", is(noticeInfo.getTitle())));
	}
	
	@Test
	@DisplayName("공지사항 저장 성공")
	public void createNoticeInfo() throws Exception {
		ResultActions resultActions = mockMvc.perform(
			multipart("/api/notice/info")
			.param("title", "test11")
			.param("contents", "test")
			.param("writer", "test")
			.param("startDate", "2024-07-24 00:00:00")
			.param("endDate", "2024-07-24 23:59:59")
			.contentType(MediaType.APPLICATION_JSON));
		
		resultActions.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.result.data", is("공지사항 등록에 성공하였습니다.")));
	}
	
	@Test
	@DisplayName("공지사항 저장 시 - 제목 중복 체크")
	public void createNoticeInfo_validationTitle() throws Exception {
		NoticeInfo noticeInfo = NoticeInfo.builder()
				.title("test111")
				.contents("test")
				.writer("test")
				.build();
		
		NoticeInfo info = noticeRepository.save(noticeInfo);
		
		ResultActions resultActions = mockMvc.perform(
			multipart("/api/notice/info")
			.param("title", info.getTitle())
			.param("contents", info.getContents())
			.param("writer", info.getWriter())
			.param("startDate", "2024-07-24 00:00:00")
			.param("endDate", "2024-07-24 23:59:59")
			.contentType(MediaType.APPLICATION_JSON));
		
		resultActions.andExpect(status().isInternalServerError())
		.andDo(print())
		.andExpect(jsonPath("$.result.data", is("이미 존재하는 공지사항입니다.")));
	}

}
