package com.project.notice.model.request;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NoticeInfoCreationRequest {

	@NotBlank(message = "제목은 필수입력 사항입니다.")
	@Size(max = 128, message = "제목의 최대 길이는 128자 입니다.")
	private String title;

	@NotBlank(message = "내용은 필수입력 사항입니다.")
	@Size(max = 1024, message = "내용의 최대 길이는 1024자 입니다.")
	private String contents;

	@Size(max = 32, message = "작성자의 최대 길이는 32자 입니다.")
	private String writer;

	@NotNull(message = "공지사항 시작일시는 필수입력 사항입니다.")
	private Timestamp startDate;

	@NotNull(message = "공지사항 종료일시는 필수입력 사항입니다.")
	private Timestamp endDate;
}
