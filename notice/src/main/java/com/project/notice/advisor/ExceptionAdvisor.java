package com.project.notice.advisor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.notice.exception.ExistException;
import com.project.notice.exception.NoticeNotFoundException;
import com.project.notice.exception.SaveFailureException;
import com.project.notice.exception.UpdateFailureException;
import com.project.notice.response.Response;

@RestControllerAdvice
public class ExceptionAdvisor {
	
	@ExceptionHandler(NoticeNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Response noticeNotFoundException() {
		return Response.failure(HttpStatus.NOT_FOUND.value(), "공지사항을 찾을 수 없습니다.");
	}
	
	@ExceptionHandler(SaveFailureException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Response saveFailureException() {
		return Response.failure(HttpStatus.NOT_FOUND.value(), "공지사항 저장에 실패하였습니다.");
	}
	
	@ExceptionHandler(UpdateFailureException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Response updateFailureException() {
		return Response.failure(HttpStatus.NOT_FOUND.value(), "공지사항 수정에 실패하였습니다.");
	}
	
	@ExceptionHandler(ExistException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Response existException() {
		return Response.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미 존재하는 공지사항입니다.");
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Response methodArgsNotValidException(MethodArgumentNotValidException e) {
		return Response.failure(HttpStatus.BAD_REQUEST.value(), e.getBindingResult().getFieldError().getDefaultMessage());
	}
}
