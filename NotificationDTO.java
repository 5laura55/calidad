package com.hiveag.geepy.dto;

public class NotificationDTO implements java.io.Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long biId;
	private int code;
	private String message;
	
	
	
	
	public NotificationDTO() {
		super();
	}
	
	
	
	public NotificationDTO(long biId, int code,String message) {
		super();
		this.biId = biId;
		this.code = code;
		this.message=message;
	}
	
	
	public long getBiId() {
		return biId;
	}
	public void setBiId(long biId) {
		this.biId = biId;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
	
	
	
	
}
