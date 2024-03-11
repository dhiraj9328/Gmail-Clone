package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserInfoRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 UserInfo user = userInfoRepository.findByUsername(username);
	        if(user == null){
	            throw new UsernameNotFoundException("could not found user..!!");
	        }
	        return new CustomUserDetails(user);
	}

}
