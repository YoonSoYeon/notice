package com.project.notice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
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
import com.project.notice.model.request.NoticeInfoUpdationRequest;
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
	public NoticeInfoDto readNoticeInfo(Long noticeNo) throws Exception {
		noticeInfoRepository.updateViewCount(noticeNo);
		NoticeInfo noticeInfo = noticeInfoRepository.findById(noticeNo).orElseThrow(NoticeNotFoundException::new);
		return this.copyToNoticeInfo(noticeInfo);
	}

	@Transactional
	public void createNoticeInfo(NoticeInfoCreationRequest noticeInfo, MultipartFile[] files) throws Exception {
		noticeInfoRepository.findByTitle(noticeInfo.getTitle()).ifPresent(t -> {
			throw new ExistException();
		});

		NoticeInfo noticeInfoToCreate = new NoticeInfo();
		BeanUtils.copyProperties(noticeInfo, noticeInfoToCreate);
		
		this.saveNotice(noticeInfoToCreate, files);
	}

	@Transactional
	public void deleteNoticeInfo(Long noticeNo) {
		NoticeInfo noticeInfo = noticeInfoRepository.findById(noticeNo).orElseThrow(NoticeNotFoundException::new);
		noticeInfoRepository.delete(noticeInfo);
	}

	@Transactional
	public void updateNoticeInfo(NoticeInfoUpdationRequest request, MultipartFile[] files, Long noticeNo) throws Exception {
		NoticeInfo noticeInfo = noticeInfoRepository.findById(noticeNo).orElseThrow(NoticeNotFoundException::new);
		noticeInfoRepository.findByTitleAndNoticeNoNot(request.getTitle(), noticeNo).ifPresent(t -> {
			throw new ExistException();
		});

		if (ObjectUtils.isEmpty(request.getTitle()) == false)
			noticeInfo.setTitle(request.getTitle());

		if (ObjectUtils.isEmpty(request.getContents()) == false)
			noticeInfo.setContents(request.getContents());

		if (request.getStartDate() != null)
			noticeInfo.setStartDate(request.getStartDate());

		if (request.getEndDate() != null)
			noticeInfo.setEndDate(request.getEndDate());

		List<NoticeFile> noticeFileToDelete = new ArrayList<NoticeFile>();
		if (noticeInfo.getNoticefiles() != null) {
			noticeFileToDelete.addAll(noticeInfo.getNoticefiles());
			noticeInfo.getNoticefiles().clear();

			this.saveNotice(noticeInfo, files);
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
	
	private void saveNotice(NoticeInfo noticeInfo, MultipartFile[] files) throws Exception {
		NoticeInfo info = noticeInfoRepository.save(noticeInfo);
		if (info == null)
			throw new SaveFailureException();

		this.setFileInfo(files, noticeInfo);
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
