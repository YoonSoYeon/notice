package com.project.notice.unit;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;

import com.project.notice.code.MessageCode;
import com.project.notice.controller.NoticeController;
import com.project.notice.model.dto.NoticeInfoDto;
import com.project.notice.model.dto.PageDto;
import com.project.notice.service.NoticeService;

@WebMvcTest(NoticeController.class)
public class NoticeControllerRestTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private NoticeService noticeService;

	@Test
	@DisplayName("공지사항 전체 조회")
	public void readNoticeInfosTest() throws Exception {
		Integer pageNo = 0;
		Integer pageSize = 3;

		PageDto pageDtoTest = new PageDto();
		List<NoticeInfoDto> noticeInfos = List.of(
			NoticeInfoDto.builder().title("test1").contents("test").writer("test").build(),
			NoticeInfoDto.builder().title("test2").contents("test").writer("test").build(),
			NoticeInfoDto.builder().title("test3").contents("test").writer("test").build()
		);
		pageDtoTest.setContent(noticeInfos);
		doReturn(pageDtoTest).when(noticeService).readNoticeInfos(pageNo, pageSize);

		mockMvc.perform(get("/api/notice/infos"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("공지사항 번호로 조회")
	public void readNoticeInfoTest() throws Exception {
		NoticeInfoDto noticeInfo = NoticeInfoDto.builder().title("test").contents("test").writer("test").build();

		doReturn(noticeInfo).when(noticeService).readNoticeInfo(anyLong());

		mockMvc.perform(get("/api/notice/info/{noticeNo}", 1))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result.data.title", is(noticeInfo.getTitle())))
			.andExpect(jsonPath("$.result.data.contents", is(noticeInfo.getContents())))
			.andExpect(jsonPath("$.result.data.writer", is(noticeInfo.getWriter())));
	}

	@Test
	@DisplayName("공지사항 저장")
	public void createNoticeInfo() throws Exception {
		MockMultipartFile file = new MockMultipartFile("test 이미지", "thumbnail.png", MediaType.IMAGE_PNG_VALUE, "thumbnail".getBytes());

		MockPart title = new MockPart("title", "test".getBytes());
		MockPart contents = new MockPart("contents", "test".getBytes());
		MockPart writer = new MockPart("writer", "test".getBytes());
		MockPart startDate = new MockPart("startDate", "2024-07-23 00:00:00".getBytes());
		MockPart endDate = new MockPart("endDate", "2024-07-23 00:00:00".getBytes());

		mockMvc.perform(multipart("/api/notice/info")
				.file(file)
				.part(title, contents, writer, startDate, endDate)
				.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result.data", is(MessageCode.NOTICE_SAVE_SUCCESS.getMessage())));

	}
	
	@Test
	@DisplayName("공지사항 저장 - 유효성 검사(제목 필수누락)")
	public void createNoticeInfo_validation() throws Exception {
		MockMultipartFile file = new MockMultipartFile("test 이미지", "thumbnail.png", MediaType.IMAGE_PNG_VALUE, "thumbnail".getBytes());

		MockPart contents = new MockPart("contents", "test".getBytes());
		MockPart writer = new MockPart("writer", "test".getBytes());
		MockPart startDate = new MockPart("startDate", "2024-07-23 00:00:00".getBytes());
		MockPart endDate = new MockPart("endDate", "2024-07-23 00:00:00".getBytes());
		
		mockMvc.perform(multipart("/api/notice/info")
				.file(file)
				.part(contents, writer, startDate, endDate)
				.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.result.msg", is("제목은 필수입력 사항입니다.")));
	}

	@Test
	@DisplayName("공지사항 삭제")
	public void deleteNoticeInfo() throws Exception {
		Long noticeNo = 1L;
		doNothing().when(noticeService).deleteNoticeInfo(noticeNo);

		mockMvc.perform(delete("/api/notice/info/{noticeNo}", noticeNo)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result.data", is(MessageCode.NOTICE_DELETE_SUCCESS.getMessage())));
	}

	@Test
	@DisplayName("공지사항 수정")
	public void updaeNoticeInfo() throws Exception {
		MockMultipartFile file = new MockMultipartFile("test 이미지", "thumbnail.png", MediaType.IMAGE_PNG_VALUE, "thumbnail".getBytes());

		MockPart title = new MockPart("title", "test".getBytes());
		MockPart contents = new MockPart("contents", "test".getBytes());
		MockPart writer = new MockPart("writer", "test".getBytes());
		MockPart startDate = new MockPart("startDate", "2024-07-23 00:00:00".getBytes());
		MockPart endDate = new MockPart("endDate", "2024-07-23 00:00:00".getBytes());

		mockMvc.perform(multipart(HttpMethod.PATCH, "/api/notice/info/{noticeNo}", 1)
				.file(file)
				.part(title, contents, writer, startDate, endDate)
				.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result.data", is(MessageCode.NOTICE_UPDATE_SUCCESS.getMessage())));
	}
}
