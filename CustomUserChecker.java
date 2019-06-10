package com.hiveag.geepy.util;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class CustomUserChecker {

	public CustomUserChecker() {
		// TODO Auto-generated constructor stub
	}
	
	public long checker(OAuth2Authentication authentication){
		
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		return customUserDetails.getPeId();
		
	}

}
