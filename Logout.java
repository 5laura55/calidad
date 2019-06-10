package com.hiveag.geepy.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Logout {

	@Autowired
	private RedisTokenStore tokenStore;

	
	   @RequestMapping(method = RequestMethod.DELETE, value = "/api/logout")
       @ResponseBody
       public void logout(HttpServletRequest request,OAuth2Authentication authentication) {
		    
			if (authentication.getDetails() != null && authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
			       OAuth2AuthenticationDetails details=(OAuth2AuthenticationDetails)authentication.getDetails();
			        String tokenValue=details.getTokenValue();
				    tokenStore.removeAccessToken(tokenValue);
				}

		}
	   
	   
	  
		
	   
}
	   