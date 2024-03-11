package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginSuccessDto;
import com.example.demo.entity.LoginDTO;
import com.example.demo.security.JwtUtil;
import com.example.demo.util.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginSuccessDto>> login(@RequestBody LoginDTO request) {
		ApiResponse<LoginSuccessDto> response = new ApiResponse<>();
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			if (authentication.isAuthenticated()) {
				LoginSuccessDto loginSuccessDto = new LoginSuccessDto();
				loginSuccessDto.setAccessToken(jwtUtil.GenerateToken(request.getUsername()));
				response.setData(loginSuccessDto);
				response.setStatus(HttpStatus.OK.value());
				return new ResponseEntity<>(response,HttpStatus.OK);
			} else {
				throw new UsernameNotFoundException("invalid user request..!!");
			}
		}catch (BadCredentialsException e) {
			e.printStackTrace();
			response.setMessage(e.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		}catch (Exception e) {
			e.printStackTrace();
			response.setMessage(e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
