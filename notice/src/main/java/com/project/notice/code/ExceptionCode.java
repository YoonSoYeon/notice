package com.project.notice.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
	NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "공지사항을 찾을 수 없습니다."),
	NOTICE_SAVE_ERROR(HttpStatus.NOT_FOUND.value(), "공지사항 저장에 실패하였습니다."),
	NOTICE_UPDATE_ERROR(HttpStatus.NOT_FOUND.value(), "공지사항 수정에 실패하였습니다."),
	
	NOTICE_EXIST(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미 존재하는 공지사항입니다."),

	NOT_VALID_ERROR(HttpStatus.BAD_REQUEST.value(), "Validation Exception 발생");

	private final int status;
	private final String message;
}
