package com.project.notice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.notice.model.request.NoticeInfoCreationRequest;
import com.project.notice.response.Response;
import com.project.notice.service.NoticeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/notice")
@RequiredArgsConstructor
public class NoticeController {

	@Autowired
	private NoticeService noticeService;

	//select notice all
	@GetMapping("/infos")
	public Response readNoticeInfos(
		@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
		@RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize) {
		return Response.success(noticeService.readNoticeInfos(pageNo, pageSize));
	}
	
	//select notice one by noticeNo
	@GetMapping("/info/{noticeNo}")
	public Response readNoticeInfo(@PathVariable("noticeNo") Long noticeNo) {
		return Response.success(noticeService.readNoticeInfo(noticeNo));
	}
	
	//create notice
	@PostMapping("/info")
	public Response createNoticeInfo (@Valid NoticeInfoCreationRequest request, @RequestParam(required=false, value="file") MultipartFile[] files) throws Exception {
		noticeService.createNoticeInfo(request, files);
		return Response.success("공지사항 등록에 성공하였습니다.");
	}
	
	//delete notice all
	@DeleteMapping("/infos")
	public Response deleteNoticeInfos () {
		noticeService.deleteNoticeInfo(null);
		return Response.success("공지사항 전체 삭제에 성공하였습니다.");
	}
	
	//delete notice one by noticeNo
	@DeleteMapping("/info/{noticeNo}")
	public Response deleteNoticeInfo (@PathVariable("noticeNo") Long noticeNo) {
		noticeService.deleteNoticeInfo(noticeNo);
		return Response.success("공지사항 삭제에 성공하였습니다.");
	}
	
	//update notice one by noticeNo
	@PatchMapping("/info/{noticeNo}")
	public Response updateNoticeInfo(NoticeInfoCreationRequest request, @RequestParam(required=false, value="file") MultipartFile[] files, @PathVariable("noticeNo") Long noticeNo) throws Exception {
		noticeService.updateNoticeInfo(request, files, noticeNo);
		return Response.success("공지사항 수정에 성공하였습니다.");
	}
}
