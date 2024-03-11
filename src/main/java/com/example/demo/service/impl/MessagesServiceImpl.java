package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CommonListDto;
import com.example.demo.dto.MessageListResponseDto;
import com.example.demo.dto.MessageRequestDto;
import com.example.demo.dto.SendMessageDto;
import com.example.demo.entity.Messages;
import com.example.demo.entity.UserInfo;
import com.example.demo.enums.ActionEnum;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.MessageService;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.PageResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
public class MessagesServiceImpl implements MessageService{

	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public ResponseEntity<ApiResponse<String>> sendMessage(SendMessageDto sendMessageDto) {
		ApiResponse<String> response = new ApiResponse<>();
		
		try {
			CustomUserDetails customUserDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Messages messages = new Messages();
			
			UserInfo userInfoTo =  userInfoRepository.findByEmail(sendMessageDto.getTo());
			if(userInfoTo == null) {
				response.setMessage("To Does not exists");
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<ApiResponse<String>>(response,HttpStatus.BAD_REQUEST);
			}
			messages.setUserTo(userInfoTo);
			messages.setUserFrom(customUserDetails.getUserInfo());
			messages.setBody(sendMessageDto.getBody());
			messages.setSubject(sendMessageDto.getSubject());
			messageRepository.save(messages);
			response.setMessage("Sent Successfully");
			response.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ApiResponse<String>>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setMessage(e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new ResponseEntity<ApiResponse<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<PageResponse<MessageListResponseDto>> getMessages(MessageRequestDto request) {
		PageResponse<MessageListResponseDto> response = new PageResponse<>();
		try {
			CustomUserDetails customUserDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Specification<Messages> rsSpeci = getMessagesList(request, customUserDetails);
			Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
			Page<Messages> rsMessages = messageRepository.findAll(rsSpeci, pageable);
			
			List<MessageListResponseDto> listResponse = new ArrayList<>();
			
			rsMessages.get().forEach(e->{
				MessageListResponseDto listResponseDto = new MessageListResponseDto();
				listResponseDto.setBody(e.getBody());
				listResponseDto.setSubject(e.getSubject());
				listResponseDto.setFirstName(e.getUserFrom().getFirstName());
				listResponseDto.setLastName(e.getUserFrom().getLastName());
				listResponseDto.setTo(e.getUserTo().getUsername());
				listResponseDto.setFrom(e.getUserFrom().getUsername());
				listResponseDto.setId(e.getId());
				listResponseDto.setIsStarred(e.getIsStarred());
				listResponseDto.setIsRead(e.getIsRead());
				listResponse.add(listResponseDto);
			});
			response.setData(listResponse);
			response.setLimit(rsMessages.getSize());
			response.setPage(rsMessages.getNumber() + 1);
			response.setTotal(rsMessages.getTotalElements());
			response.setTotalPage(rsMessages.getTotalPages());
			response.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<PageResponse<MessageListResponseDto>>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage(e.getMessage());
			return new ResponseEntity<PageResponse<MessageListResponseDto>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public Specification<Messages> getMessagesList(MessageRequestDto request,CustomUserDetails customUserDetails){
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			String search = request.getSearch();
			
			if(search != null) {
				search = "%" + search + "%";
				predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get("subject"), search),
						criteriaBuilder.like(root.get("body"), search)));
				 Join<Messages,UserInfo> userJoin = root.join("userFrom");
				 predicates.add(criteriaBuilder.like(userJoin.get("username"), search));
				 predicates.add(criteriaBuilder.like(userJoin.get("email"), search));
			}
			
			if(request.getIsInbox()) {
				Join<Messages,UserInfo> userJoin = root.join("userTo");
				System.out.println(customUserDetails.getUserInfo().getId());
				predicates.add(criteriaBuilder.equal(userJoin.get("id"), customUserDetails.getUserInfo().getId()));
				predicates.add(criteriaBuilder.equal(root.get("isTrash"), Boolean.FALSE));
			}else if(request.getIsSent()) {
				Join<Messages,UserInfo> userJoin = root.join("userFrom");
				predicates.add(criteriaBuilder.equal(userJoin.get("id"), customUserDetails.getUserInfo().getId()));
				predicates.add(criteriaBuilder.equal(root.get("isTrash"), Boolean.FALSE));
			}else if(request.getIsTrash()) {
				Join<Messages,UserInfo> userJoin = root.join("userTo");
				predicates.add(criteriaBuilder.equal(userJoin.get("id"), customUserDetails.getUserInfo().getId()));
				predicates.add(criteriaBuilder.equal(root.get("isTrash"), Boolean.TRUE));
			}else if(request.getIsStarred()) {
				predicates.add(criteriaBuilder.equal(root.get("isStarred"), Boolean.TRUE));
				predicates.add(criteriaBuilder.equal(root.get("isTrash"), Boolean.FALSE));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};	
	}

	@Override
	public ResponseEntity<ApiResponse<String>> starredMessage(Long id) {
		ApiResponse<String> response = new ApiResponse<>();
		try {
			Optional<Messages> rsOptional = messageRepository.findById(id);
			if (!rsOptional.isPresent()) {
				response.setMessage("Message not found");
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<ApiResponse<String>>(response, HttpStatus.BAD_REQUEST);
			}
			Messages message = rsOptional.get();
			message.setIsStarred(!message.getIsStarred());
			messageRepository.save(message);
			response.setMessage("Updated Starred Successfully");
			response.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ApiResponse<String>>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setMessage(e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new ResponseEntity<ApiResponse<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional 
	@Override
	public ResponseEntity<ApiResponse<String>> bulkActionMessages(CommonListDto request) {
		ApiResponse<String> response = new ApiResponse<>();
		try {
			updateMessages(request.getAction().getFlag(), request.getFlag(), request.getIdList());
			response.setMessage("Updated Successfully");
			response.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ApiResponse<String>>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage(e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new ResponseEntity<ApiResponse<String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public void updateMessages(String fieldName, Boolean value, List<Long> ids) {
	    StringBuilder queryBuilder = new StringBuilder();
	    queryBuilder.append("UPDATE messages SET ")
	                .append(fieldName).append(" = :flag ")
	                .append("WHERE id IN :ids");

	    Query query = entityManager.createNativeQuery(queryBuilder.toString());
	    query.setParameter("flag", value);
	    query.setParameter("ids", ids);
	    
	    query.executeUpdate();
	}

	@Override
	public ResponseEntity<ApiResponse<MessageListResponseDto>> getMessageDetail(Long id, Boolean inbox) {
		ApiResponse<MessageListResponseDto> response = new ApiResponse<>();
		try {
			CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			if (id == null) {
				response.setMessage("Id is required");
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<ApiResponse<MessageListResponseDto>>(response, HttpStatus.BAD_REQUEST);
			}

			Messages message = null;
			if (!inbox) {
				message = messageRepository.findByIdAndUserFrom(id, customUserDetails.getUserInfo());
			} else {
				message = messageRepository.findByIdAndUserTo(id, customUserDetails.getUserInfo());
			}
			if (message == null) {
				response.setMessage("Message not found");
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<ApiResponse<MessageListResponseDto>>(response, HttpStatus.BAD_REQUEST);
			}
			MessageListResponseDto messageDto = new MessageListResponseDto();
			messageDto.setFrom(message.getUserFrom().getUsername());
			messageDto.setTo(message.getUserTo().getUsername());
			messageDto.setSubject(message.getSubject());
			messageDto.setBody(message.getBody());

			response.setData(messageDto);
			response.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ApiResponse<MessageListResponseDto>>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage(e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new ResponseEntity<ApiResponse<MessageListResponseDto>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<ApiResponse<String>> permanentDelete(CommonListDto request) {
		ApiResponse<String> response = new ApiResponse<>();
		try {
			CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			List<Messages> messageList = messageRepository.findByIdInAndUserTo(request.getIdList(), customUserDetails.getUserInfo());
			
			messageRepository.deleteAll(messageList);
			response.setMessage("Deleted Successfully");
			response.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ApiResponse<String>>(response, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setMessage(e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new ResponseEntity<ApiResponse<String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
