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
	@DisplayName("공지사항 번호로 조회 테스트")
	public void selectNoticeByNoTest() {
		// given
		NoticeInfo saved = noticeInfoRepository.save(NoticeInfo.builder().title("test").contents("test").writer("test").build());

		// when
		Optional<NoticeInfo> optionalNotice = noticeInfoRepository.findById(saved.getNoticeNo());
		
		//then
		assertEquals("test", optionalNotice.get().getTitle());
	}
	
	@Test
	@DisplayName("존재하지 않는 공지사항 번호로 조회 테스트")
	public void selectNotice_NotExistNoticeNoTest() {
		// given
		NoticeInfo saved = noticeInfoRepository.save(NoticeInfo.builder().title("test").contents("test").writer("test").build());
		noticeInfoRepository.deleteById(saved.getNoticeNo());

		// when
		Optional<NoticeInfo> optionalNotice = noticeInfoRepository.findById(saved.getNoticeNo());
		
		// then
		assertTrue(optionalNotice.isEmpty());
	}

	@Test
	@DisplayName("공지사항 전체 조회 테스트")
	public void selectAllNoticeTest() {
		// given
		NoticeInfo saved1 = noticeInfoRepository.save(NoticeInfo.builder().title("test1").contents("test").writer("test").build());
		NoticeInfo saved2 = noticeInfoRepository.save(NoticeInfo.builder().title("test2").contents("test").writer("test").build());
		NoticeInfo saved3 = noticeInfoRepository.save(NoticeInfo.builder().title("test3").contents("test").writer("test").build());
		NoticeInfo saved4 = noticeInfoRepository.save(NoticeInfo.builder().title("test4").contents("test").writer("test").build());
		NoticeInfo saved5 = noticeInfoRepository.save(NoticeInfo.builder().title("test5").contents("test").writer("test").build());

		// when
		List<NoticeInfo> noticeInfo = noticeInfoRepository.findAllById(List.of(saved1.getNoticeNo(), saved2.getNoticeNo(), saved3.getNoticeNo(), saved4.getNoticeNo(), saved5.getNoticeNo()));
		
		//then
		assertEquals(5, noticeInfo.size());
	}

	@Test
	@DisplayName("공지사항 저장 테스트")
	public void saveNoticeTest() {
		//given
		NoticeInfo noticeInfo = NoticeInfo.builder().title("test").contents("test").writer("test").build();
		
		//when
		NoticeInfo savedInfo = noticeInfoRepository.save(noticeInfo);

		//then
		assertNotNull(savedInfo);
		assertTrue(noticeInfo.getTitle().equals(savedInfo.getTitle()));
		assertTrue(noticeInfo.getContents().equals(savedInfo.getContents()));
		assertTrue(noticeInfo.getWriter().equals(savedInfo.getWriter()));
	}
	
	@Test
	@DisplayName("공지사항 수정 테스트")
	public void updateNoticeTest() {
		//given
		NoticeInfo saved = noticeInfoRepository.save(NoticeInfo.builder().title("test1").contents("test").writer("test").build());
		
		//when
		NoticeInfo updated = noticeInfoRepository.save(NoticeInfo.builder().title("test2").contents(saved.getContents()).writer(saved.getWriter()).build());
		
		//then
		assertEquals("test2", updated.getTitle());
	}
	
	@Test
	@DisplayName("공지사항 제목 중복 조회 테스트")
	public void existNoticeTitleTest() {
		//given
		NoticeInfo saved = noticeInfoRepository.save(NoticeInfo.builder().title("test1").contents("test").writer("test").build());
		
		//when
		Optional<NoticeInfo> updated = noticeInfoRepository.findByTitle(saved.getTitle());
		
		//then
		assertNotNull(updated.isPresent());
	}

	@Test
	@DisplayName("조회수 수정 테스트")
	public void updateViewCountTest() {
		// given
		NoticeInfo saved = noticeInfoRepository.save(NoticeInfo.builder().title("test1").contents("test").writer("test").build());
		entityManager.flush();
		entityManager.clear();
		
		//when
		noticeInfoRepository.updateViewCount(saved.getNoticeNo());

		//then
		Optional<NoticeInfo> selectNotice = noticeInfoRepository.findById(saved.getNoticeNo());
		assertEquals(1, selectNotice.get().getViewCount());
	}

	@Test
	@DisplayName("공지사항 삭제 테스트")
	public void deleteNoticeTest() {
		//given
		NoticeInfo saved = noticeInfoRepository.save(NoticeInfo.builder().title("test").contents("test").writer("test").build());
		assertEquals("test", saved.getTitle());
		
		//when
		noticeInfoRepository.deleteById(saved.getNoticeNo());

		//then
		assertTrue(noticeInfoRepository.findById(saved.getNoticeNo()).isEmpty());
	}
}
