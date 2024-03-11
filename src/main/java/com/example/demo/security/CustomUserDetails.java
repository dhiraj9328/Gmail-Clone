package com.example.demo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.UserInfo;

public class CustomUserDetails extends UserInfo implements UserDetails{

	private String username;
    private String password;
    
    private UserInfo userInfo;
  
    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo byUsername) {
        this.username = byUsername.getUsername();
        this.password= byUsername.getPassword();
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority(byUsername.getRole().toUpperCase()));
        this.authorities = auths;
        this.userInfo = byUsername;
    }
    
    public UserInfo getUserInfo() {
    	return this.userInfo;
    }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	
}
