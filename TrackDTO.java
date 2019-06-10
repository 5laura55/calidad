package com.hiveag.geepy.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.pojo.Track;
import com.hiveag.geepy.util.View;

public class TrackDTO  implements java.io.Serializable {
   
	private static final long serialVersionUID = 8190072712162847507L;
	
	private Track track;
	private int battery;
	private int signal;
	
	
	public TrackDTO(Track track, int battery, int signal) {
		super();
		this.track = track;
		this.battery = battery;
		this.signal = signal;
	}
	
	public TrackDTO() {
		super();
	}
	
	@JsonView(View.Summary.class)
	public Track getTrack() {
		return track;
	}
	public void setTrack(Track track) {
		this.track = track;
	}
	
	@JsonView(View.Summary.class)
	public int getBattery() {
		return battery;
	}
	public void setBattery(int battery) {
		this.battery = battery;
	}

	@JsonView(View.Summary.class)
	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
	
	
	
   
}
