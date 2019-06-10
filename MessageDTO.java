package com.hiveag.geepy.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.util.View;

public class MessageDTO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4390974617905690877L;	
	private HttpStatus meCode;
	private String meMessage;
	
	public MessageDTO(){		
	
	}
	
	@JsonView(View.Summary.class)
	public HttpStatus getMeCode() {
		return meCode;
	}
	
	
	public void setMeCode(HttpStatus meCode) {
		this.meCode = meCode;
	}
	
	@JsonView(View.Summary.class)
	public String getMeMessage() {
		return meMessage;
	}
	
	
	public void setMeMessage(String meMessage) {
		this.meMessage = meMessage;
	}
}
