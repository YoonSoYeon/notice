package com.project.notice.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageCode {
	NOTICE_SAVE_SUCCESS(HttpStatus.OK.value(), "공지사항 등록에 성공하였습니다."),
	NOTICE_UPDATE_SUCCESS(HttpStatus.OK.value(), "공지사항 수정에 성공하였습니다."),
	NOTICE_DELETE_SUCCESS(HttpStatus.OK.value(), "공지사항 삭제에 성공하였습니다.");
	
	private final int status;
	private final String message;
}
