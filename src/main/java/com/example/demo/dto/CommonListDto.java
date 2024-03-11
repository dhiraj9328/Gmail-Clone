package com.example.demo.dto;

import java.util.List;

import com.example.demo.enums.ActionEnum;

public class CommonListDto {

	private List<Long> idList;
	 
	private ActionEnum action;
	
	private Boolean flag;
	
	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

	public ActionEnum getAction() {
		return action;
	}

	public void setAction(ActionEnum actionEnum) {
		this.action = actionEnum;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
}
