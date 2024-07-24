package com.project.notice.advisor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.notice.code.ExceptionCode;
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
		return Response.failure(ExceptionCode.NOTICE_NOT_FOUND.getStatus(), ExceptionCode.NOTICE_NOT_FOUND.getMessage());
	}

	@ExceptionHandler(SaveFailureException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Response saveFailureException() {
		return Response.failure(ExceptionCode.NOTICE_SAVE_ERROR.getStatus(), ExceptionCode.NOTICE_SAVE_ERROR.getMessage());
	}

	@ExceptionHandler(UpdateFailureException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Response updateFailureException() {
		return Response.failure(ExceptionCode.NOTICE_UPDATE_ERROR.getStatus(), ExceptionCode.NOTICE_UPDATE_ERROR.getMessage());
	}

	@ExceptionHandler(ExistException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Response existException() {
		return Response.failure(ExceptionCode.NOTICE_EXIST.getStatus(), ExceptionCode.NOTICE_EXIST.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Response methodArgsNotValidException(MethodArgumentNotValidException e) {
		return Response.failure(ExceptionCode.NOT_VALID_ERROR.getStatus(), e.getBindingResult().getFieldError().getDefaultMessage());
	}
}
