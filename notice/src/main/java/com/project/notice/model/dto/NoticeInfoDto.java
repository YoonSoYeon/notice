package com.project.notice.model.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeInfoDto {
	private String title;
	private String contents;
	private String writer;
	private Timestamp createDate;
	private Long viewCount;
}
