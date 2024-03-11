package com.example.demo.enums;

public enum ActionEnum {

	DELETE("is_trash"),READ("is_read"),UNREAD("is_read"),STARRED("is_starred"),UNSTARRED("is_starred");

	private String flag;
	
	ActionEnum(String flag) {
		this.flag = flag;
	}

	public String getFlag() {
		return flag;
	}
}
