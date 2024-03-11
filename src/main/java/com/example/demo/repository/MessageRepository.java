package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Messages;
import com.example.demo.entity.UserInfo;

public interface MessageRepository extends JpaRepository<Messages, Long>,JpaSpecificationExecutor<Messages>{
	
	public void deleteByIdIn(List<Long> ids);
	
	public Messages findByIdAndUserTo(Long id,UserInfo userTo);
	
	public Messages findByIdAndUserFrom(Long id,UserInfo userTo);
	
	public List<Messages> findByIdInAndUserTo(List<Long> idList,UserInfo userTo);

}
