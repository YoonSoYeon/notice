package com.project.notice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "notice_file")
public class NoticeFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fileNo;

	@Column(nullable = false, length = 256)
	private String fileName;

	@Column(columnDefinition = "bytea")
	private byte[] attachFile;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "notice_no")
	@JsonManagedReference
	private NoticeInfo noticeInfo;
}
