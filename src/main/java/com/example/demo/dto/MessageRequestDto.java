package com.example.demo.dto;

public class MessageRequestDto {

	private Integer page = 0;
	
	private Integer size = 50;
	
	private Boolean isInbox = Boolean.FALSE;
	
	private Boolean isTrash= Boolean.FALSE;
	
	private Boolean isDraft= Boolean.FALSE;
	
	private Boolean isSent= Boolean.FALSE;
	
	private Boolean isStarred= Boolean.FALSE;
	
	private String search;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Boolean getIsInbox() {
		return isInbox;
	}

	public void setIsInbox(Boolean isInbox) {
		this.isInbox = isInbox;
	}

	public Boolean getIsTrash() {
		return isTrash;
	}

	public void setIsTrash(Boolean isTrash) {
		this.isTrash = isTrash;
	}

	public Boolean getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(Boolean isDraft) {
		this.isDraft = isDraft;
	}

	public Boolean getIsSent() {
		return isSent;
	}

	public void setIsSent(Boolean isSent) {
		this.isSent = isSent;
	}

	public Boolean getIsStarred() {
		return isStarred;
	}

	public void setIsStarred(Boolean isStarred) {
		this.isStarred = isStarred;
	}
	
}
