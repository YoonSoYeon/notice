package com.project.notice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.project.notice.model.NoticeInfo;
import com.project.notice.repository.NoticeInfoRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NoticeInfoRepositoryTest {

	@Autowired
	NoticeInfoRepository noticeInfoRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	@DisplayName("공지사항 번호로 조회")
	public void selectNoticeByNo() {
		//공지사항 등록
		NoticeInfo saved = noticeInfoRepository.save(NoticeInfo.builder().title("test").contents("test").writer("test").build());
		assertEquals("test", saved.getTitle());
		
		//noticeNo 검색
		Optional<NoticeInfo> optionalNotice = noticeInfoRepository.findById(saved.getNoticeNo());
		assertEquals("test", optionalNotice.get().getTitle());

		//공지사항 삭제 후 검색
		noticeInfoRepository.deleteById(saved.getNoticeNo());
		Optional<NoticeInfo> deletedNotice = noticeInfoRepository.findById(saved.getNoticeNo());
		assertTrue(deletedNotice.isEmpty());
	}
	
	@Test
	@DisplayName("공지사항 전체 조회")
	public void selectAllNotice() {
		//공지사항 등록
		NoticeInfo saved1 = noticeInfoRepository.save(NoticeInfo.builder().title("test1").contents("test").writer("test").build());
		NoticeInfo saved2 = noticeInfoRepository.save(NoticeInfo.builder().title("test2").contents("test").writer("test").build());
		NoticeInfo saved3 = noticeInfoRepository.save(NoticeInfo.builder().title("test3").contents("test").writer("test").build());
		NoticeInfo saved4 = noticeInfoRepository.save(NoticeInfo.builder().title("test4").contents("test").writer("test").build());
		NoticeInfo saved5 = noticeInfoRepository.save(NoticeInfo.builder().title("test5").contents("test").writer("test").build());
		
		entityManager.flush();
		entityManager.clear();
		
		//공지사항 검색
		List<NoticeInfo> noticeInfo = noticeInfoRepository.findAllById(List.of(saved1.getNoticeNo(), saved2.getNoticeNo(), saved3.getNoticeNo(), saved4.getNoticeNo(), saved5.getNoticeNo()));
		assertEquals(5, noticeInfo.size());
	}
	
	@Test
	@DisplayName("공지사항 저장 테스트")
	public void saveNoticeTest() {
		NoticeInfo noticeInfo = NoticeInfo.builder().title("test").contents("test").writer("test").build();
		NoticeInfo savedInfo = noticeInfoRepository.save(NoticeInfo.builder().title("test").contents("test").writer("test").build());

		assertNotNull(savedInfo);
		assertTrue(noticeInfo.getTitle().equals(savedInfo.getTitle()));
		assertTrue(noticeInfo.getContents().equals(savedInfo.getContents()));
		assertTrue(noticeInfo.getWriter().equals(savedInfo.getWriter()));
	}
	
	@Test
	@DisplayName("조회수 수정 테스트")
	public void updateViewCount() {
		//공지사항 등록
		NoticeInfo saved = noticeInfoRepository.save(NoticeInfo.builder().title("test").contents("test").writer("test").build());
		assertEquals("test", saved.getTitle());
		
		entityManager.flush();
		entityManager.clear();
		
		//공지사항 수정
		NoticeInfo updated = noticeInfoRepository.save(NoticeInfo.builder().title("test2").contents(saved.getContents()).writer(saved.getWriter()).build());
		noticeInfoRepository.updateViewCount(saved.getNoticeNo());
		assertEquals("test2", updated.getTitle());
		
		Optional<NoticeInfo> selectNotice = noticeInfoRepository.findById(saved.getNoticeNo());
		assertEquals(1, selectNotice.get().getViewCount());
	}
	
	@Test
	@DisplayName("공지사항 삭제 테스트")
	public void deleteNotice() {
		NoticeInfo savedInfo = noticeInfoRepository.save(NoticeInfo.builder().title("test").contents("test").writer("test").build());
		assertEquals("test", savedInfo.getTitle());
		
		noticeInfoRepository.deleteById(savedInfo.getNoticeNo());
		
		assertTrue(noticeInfoRepository.findById(savedInfo.getNoticeNo()).isEmpty());
	}
}
