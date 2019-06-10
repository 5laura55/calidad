package com.hiveag.geepy.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.util.View;

public class PasswordDTO implements java.io.Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String currentPassword;
	private String newPassword;
	
	
	public PasswordDTO() {}
	
	public PasswordDTO( String currentPassword, String newPassword) {
		
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}
	
	@JsonView(View.Summary.class)
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	
	@JsonView(View.Summary.class)
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
}
