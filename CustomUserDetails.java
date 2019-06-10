package com.hiveag.geepy.util;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 7913447754419953169L;
	private long peId;
	private String peUsername;
	private String pePassword;
	private List<GrantedAuthority> peAuthority;
	
	public CustomUserDetails(long peId, String username, String password, List<GrantedAuthority> authority){
		this.peId = peId;
		this.peUsername = username;
		this.pePassword = password;
		this.peAuthority = authority;
	}
	
	
	public long getPeId() {
		return peId;
	}


	public void setPeId(long peId) {
		this.peId = peId;
	}


	public String getPassword() {
		// TODO Auto-generated method stub
		return this.pePassword;
	}

	public String getUsername() {
		// TODO Auto-generated method stub
		return this.peUsername;
	}


	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}


	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.peAuthority;
	}

}
