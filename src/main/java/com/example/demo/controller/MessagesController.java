package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CommonListDto;
import com.example.demo.dto.MessageListResponseDto;
import com.example.demo.dto.MessageRequestDto;
import com.example.demo.dto.SendMessageDto;
import com.example.demo.enums.ActionEnum;
import com.example.demo.service.MessageService;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.PageResponse;

@RestController
@RequestMapping("/api/message")
public class MessagesController {

	@Autowired
	private MessageService messageService;
	
	@PostMapping("/send-message")
	private ResponseEntity<ApiResponse<String>> sendMessage(@RequestBody SendMessageDto sendMessageDto){
		return messageService.sendMessage(sendMessageDto);
	} 
	
	@GetMapping("/get-message")
	private ResponseEntity<PageResponse<MessageListResponseDto>> getMessages(MessageRequestDto request){
		return messageService.getMessages(request);
	}
	
	@GetMapping("/detail")
	private ResponseEntity<ApiResponse<MessageListResponseDto>> getMessageDetail(@RequestParam Long id,@RequestParam(required = true,defaultValue = "true") Boolean inbox){
		return messageService.getMessageDetail(id,inbox);
	}
	
	@PutMapping("/mark-starred")
	private ResponseEntity<ApiResponse<String>> starredMessage(@RequestParam Long id){
		return messageService.starredMessage(id);
	}
	
	@PostMapping("/bulk-action")
	private ResponseEntity<ApiResponse<String>> deleteMessages(@RequestBody CommonListDto request){
		return messageService.bulkActionMessages(request);
	}
	
	@PostMapping("/permanent-delete")
	private ResponseEntity<ApiResponse<String>> permanentDelete(@RequestBody CommonListDto request){
		return messageService.permanentDelete(request);
	}
	
}
