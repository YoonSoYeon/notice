package com.project.notice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.notice.exception.ExistException;
import com.project.notice.exception.NoticeNotFoundException;
import com.project.notice.exception.SaveFailureException;
import com.project.notice.exception.UpdateFailureException;
import com.project.notice.model.NoticeFile;
import com.project.notice.model.NoticeInfo;
import com.project.notice.model.dto.NoticeInfoDto;
import com.project.notice.model.dto.PageDto;
import com.project.notice.model.request.NoticeInfoCreationRequest;
import com.project.notice.repository.NoticeInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeInfoRepository noticeInfoRepository;

	public PageDto readNoticeInfos(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("noticeNo").ascending());
		Page<NoticeInfo> noticePage = noticeInfoRepository.findAll(pageable);

		List<NoticeInfoDto> noticeResponse = noticePage.getContent().stream().map(n -> this.copyToNoticeInfo(n)).collect(Collectors.toList());
		
		return PageDto.builder().content(noticeResponse).pageNo(pageNo).pageSize(pageSize).totalElements(noticePage.getTotalElements()).totalPages(noticePage.getTotalPages()).last(noticePage.isLast()).build();
	}

	@Transactional
	public NoticeInfoDto readNoticeInfo(Long noticeNo) {
		noticeInfoRepository.updateViewCount(noticeNo);
		NoticeInfo noticeInfo = noticeInfoRepository.findById(noticeNo).orElseThrow(NoticeNotFoundException::new);
		return this.copyToNoticeInfo(noticeInfo);
	}

	@Transactional
	public void createNoticeInfo(NoticeInfoCreationRequest noticeInfo, MultipartFile[] files) throws Exception {
		Optional<NoticeInfo> optionalNoticeInfoByTitle = noticeInfoRepository.findByTitle(noticeInfo.getTitle());
		if (optionalNoticeInfoByTitle.isPresent() == false) {
			NoticeInfo noticeInfoToCreate = new NoticeInfo();
			BeanUtils.copyProperties(noticeInfo, noticeInfoToCreate);
			NoticeInfo info = noticeInfoRepository.save(noticeInfoToCreate);
			if (info == null)
				throw new SaveFailureException();

			this.setFileInfo(files, noticeInfoToCreate);
			noticeInfoRepository.flush();
		} else {
			throw new ExistException();
		}
	}

	@Transactional
	public void deleteNoticeInfo(Long noticeNo) {
		if (noticeNo == null)
			noticeInfoRepository.deleteAll();
		else
			noticeInfoRepository.deleteById(noticeNo);
	}

	@Transactional
	public void updateNoticeInfo(NoticeInfoCreationRequest request, MultipartFile[] files, Long noticeNo) throws Exception {
		NoticeInfo noticeInfo = noticeInfoRepository.findById(noticeNo).orElseThrow(NoticeNotFoundException::new);

		if (request.getContents() != null && request.getContents().isEmpty() == false)
			noticeInfo.setContents(request.getContents());

		if (request.getStartDate() != null)
			noticeInfo.setStartDate(request.getStartDate());

		if (request.getEndDate() != null)
			noticeInfo.setEndDate(request.getEndDate());

		List<NoticeFile> noticeFileToDelete = new ArrayList<NoticeFile>();
		if (noticeInfo.getNoticefiles() != null) {
			noticeFileToDelete.addAll(noticeInfo.getNoticefiles());
			noticeInfo.getNoticefiles().clear();
			noticeInfoRepository.save(noticeInfo);
			this.setFileInfo(files, noticeInfo);
			noticeInfoRepository.flush();
			noticeInfo.getNoticefiles().removeAll(noticeFileToDelete);
		} else {
			throw new UpdateFailureException();
		}
	}
	
	private NoticeInfoDto copyToNoticeInfo(NoticeInfo noticeInfo) {
		NoticeInfoDto noticeInfoDto = new NoticeInfoDto();
		BeanUtils.copyProperties(noticeInfo, noticeInfoDto);
		return noticeInfoDto;
	}
	
	private void setFileInfo(MultipartFile[] files, NoticeInfo noticeInfo) throws Exception {
		if (files != null && files.length > 0) {
			for (MultipartFile file : files) {
				if (file.isEmpty() == false) {
					NoticeFile noticeFile = new NoticeFile();
					noticeFile.setFileName(file.getOriginalFilename());
					noticeFile.setAttachFile(file.getBytes());
					noticeFile.setNoticeInfo(noticeInfo);
					noticeInfo.getNoticefiles().add(noticeFile);
				}
			}
		}
	}
}
