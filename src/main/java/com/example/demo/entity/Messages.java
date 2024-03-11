package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Messages {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_to")
	private UserInfo userTo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_from")
	private UserInfo userFrom;
	
	private String subject;
	
	private String body;
	
	private Boolean isTrash = Boolean.FALSE;
	
	private Boolean isStarred = Boolean.FALSE;
	
	private Boolean isRead = Boolean.FALSE;

	public Boolean getIsTrash() {
		return isTrash;
	}

	public void setIsTrash(Boolean isTrash) {
		this.isTrash = isTrash;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserInfo getUserTo() {
		return userTo;
	}

	public void setUserTo(UserInfo userTo) {
		this.userTo = userTo;
	}

	public UserInfo getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(UserInfo userFrom) {
		this.userFrom = userFrom;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Boolean getIsStarred() {
		return isStarred;
	}

	public void setIsStarred(Boolean isStarred) {
		this.isStarred = isStarred;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
}
