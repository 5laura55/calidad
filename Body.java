package com.hiveag.geepy.dto;

import java.util.Date;

public class Body implements java.io.Serializable {
   
	
	/*informacion de geepy con el track*/
	
	
	private static final long serialVersionUID = 1L;
	private double latitude;
	private double longitude;
	private Date time_gps;
	private int battery;
	private int signal;
	private String  message;
	private Long biId;
	private char biStatus;
	private int code;
	

	/*punto geocerca*/
	private Double biGeoLat;
 	private Double biGeoLon;
 	private Short radius;
 	private char biGeoStatus;
	
	
 	
	public Body(){
		super();
	}



	public Body(double latitude, double longitude, Date time_gps, int battery, int signal, String message, Long biId,
			char biStatus, Double biGeoLat, Double biGeoLon, Short radius, char biGeoStatus) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.time_gps = time_gps;
		this.battery = battery;
		this.signal = signal;
		this.message = message;
		this.biId = biId;
		this.biStatus = biStatus;
		this.biGeoLat = biGeoLat;
		this.biGeoLon = biGeoLon;
		this.radius = radius;
		this.biGeoStatus = biGeoStatus;
	}


	public int getCode() {
		return code;
	}



	public void setCode(int code) {
		this.code = code;
	}

	public double getLatitude() {
		return latitude;
	}



	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}



	public double getLongitude() {
		return longitude;
	}



	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}



	public Date getTime_gps() {
		return time_gps;
	}



	public void setTime_gps(Date time_gps) {
		this.time_gps = time_gps;
	}



	public int getBattery() {
		return battery;
	}



	public void setBattery(int battery) {
		this.battery = battery;
	}



	public int getSignal() {
		return signal;
	}



	public void setSignal(int signal) {
		this.signal = signal;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public Long getBiId() {
		return biId;
	}



	public void setBiId(Long biId) {
		this.biId = biId;
	}



	public char getBiStatus() {
		return biStatus;
	}



	public void setBiStatus(char biStatus) {
		this.biStatus = biStatus;
	}



	public Double getBiGeoLat() {
		return biGeoLat;
	}



	public void setBiGeoLat(Double biGeoLat) {
		this.biGeoLat = biGeoLat;
	}



	public Double getBiGeoLon() {
		return biGeoLon;
	}



	public void setBiGeoLon(Double biGeoLon) {
		this.biGeoLon = biGeoLon;
	}



	public Short getRadius() {
		return radius;
	}



	public void setRadius(Short radius) {
		this.radius = radius;
	}



	public char getBiGeoStatus() {
		return biGeoStatus;
	}



	public void setBiGeoStatus(char biGeoStatus) {
		this.biGeoStatus = biGeoStatus;
	}

	
}
