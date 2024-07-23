package com.project.notice.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@DynamicInsert
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notice_info")
public class NoticeInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long noticeNo;
	
	@Column(nullable = false, length = 128)
	private String title;
	
	@Column(nullable = false, length = 1024)
	private String contents;
	
	@Column(nullable = false, length = 10)
	private String writer;
	
	private Timestamp startDate;
	private Timestamp endDate;
	
	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp createDate;
	
	@ColumnDefault("0")
	private Long viewCount;

	@JsonBackReference
	@OneToMany(mappedBy = "noticeInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NoticeFile> Noticefiles = new ArrayList<NoticeFile>();
}
