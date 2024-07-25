package com.project.notice.model.request;

import java.sql.Timestamp;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NoticeInfoUpdationRequest {

	@Size(max = 128, message = "제목의 최대 길이는 128자 입니다.")
	private String title;

	@Size(max = 1024, message = "내용의 최대 길이는 1024자 입니다.")
	private String contents;

	private Timestamp startDate;

	private Timestamp endDate;
}
