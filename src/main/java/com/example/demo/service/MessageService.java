package com.example.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.CommonListDto;
import com.example.demo.dto.MessageListResponseDto;
import com.example.demo.dto.MessageRequestDto;
import com.example.demo.dto.SendMessageDto;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.PageResponse;

public interface MessageService {

	public ResponseEntity<ApiResponse<String>> sendMessage(@RequestBody SendMessageDto sendMessageDto);
	
	public ResponseEntity<PageResponse<MessageListResponseDto>> getMessages(MessageRequestDto request);
	
	public ResponseEntity<ApiResponse<String>> starredMessage(Long id);
	
	public ResponseEntity<ApiResponse<String>> bulkActionMessages( CommonListDto request);
	
	public ResponseEntity<ApiResponse<String>> permanentDelete(CommonListDto request);
	
	ResponseEntity<ApiResponse<MessageListResponseDto>> getMessageDetail(Long id,Boolean inbox);
}
