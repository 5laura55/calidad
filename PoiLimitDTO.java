package com.hiveag.geepy.dto;

public class PoiLimitDTO  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double lat;
	private double lng;
	private double distance;
	private String[] poTypes;
	
	
	public PoiLimitDTO() {
		super();
	}
	
	


	public PoiLimitDTO(double lat, double lng, double distance, String[] poTypes) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.distance = distance;
		this.poTypes = poTypes;
	}




	public double getLat() {
		return lat;
	}


	public void setLat(double lat) {
		this.lat = lat;
	}


	public double getLng() {
		return lng;
	}


	public void setLng(double lng) {
		this.lng = lng;
	}


	public double getDistance() {
		return distance;
	}


	public void setDistance(double distance) {
		this.distance = distance;
	}


	public String[] getPoTypes() {
		return poTypes;
	}


	public void setPoTypes(String[] poTypes) {
		this.poTypes = poTypes;
	}
	
	
	

}
