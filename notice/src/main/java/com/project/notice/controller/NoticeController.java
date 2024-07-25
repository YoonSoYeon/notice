package com.project.notice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.notice.WebUtil;
import com.project.notice.code.MessageCode;
import com.project.notice.model.request.NoticeInfoCreationRequest;
import com.project.notice.model.request.NoticeInfoUpdationRequest;
import com.project.notice.response.Response;
import com.project.notice.service.NoticeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/notice")
@RequiredArgsConstructor
@Validated
public class NoticeController {

	@Autowired
	private NoticeService noticeService;

	// select notice all
	@GetMapping("/infos")
	public Response readNoticeInfos(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo, @RequestParam(value = "pageSize", defaultValue = "100", required = false) int pageSize) throws Exception {
		return Response.success(noticeService.readNoticeInfos(pageNo, pageSize));
	}

	// select notice one by noticeNo
	@GetMapping("/info/{noticeNo}")
	public Response readNoticeInfo(@PathVariable("noticeNo") Long noticeNo) throws Exception {
		return Response.success(noticeService.readNoticeInfo(noticeNo));
	}

	// create notice
	@PostMapping(value = "/info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response createNoticeInfo(HttpServletRequest httpRequest, @Valid NoticeInfoCreationRequest request, @RequestParam(required = false, value = "file") MultipartFile[] files) throws Exception {
		request.setWriter(WebUtil.getClientIpAddress(httpRequest));
		noticeService.createNoticeInfo(request, files);
		return Response.success(MessageCode.NOTICE_SAVE_SUCCESS.getMessage());
	}

	// delete notice one by noticeNo
	@DeleteMapping("/info/{noticeNo}")
	public Response deleteNoticeInfo(@PathVariable("noticeNo") Long noticeNo) throws Exception {
		noticeService.deleteNoticeInfo(noticeNo);
		return Response.success(MessageCode.NOTICE_DELETE_SUCCESS.getMessage());
	}

	// update notice one by noticeNo
	@PatchMapping(value = "/info/{noticeNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNoticeInfo(@Valid NoticeInfoUpdationRequest request, @RequestParam(required = false, value = "file") MultipartFile[] files, @PathVariable("noticeNo") Long noticeNo) throws Exception {
		noticeService.updateNoticeInfo(request, files, noticeNo);
		return Response.success(MessageCode.NOTICE_UPDATE_SUCCESS.getMessage());
	}
}
