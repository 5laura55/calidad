package com.hiveag.geepy.dto;

import java.io.Serializable;
import com.hiveag.geepy.pojo.EndPoint;
import com.hiveag.geepy.pojo.MultipartInfo;
import com.hiveag.geepy.pojo.Organization;

public class ResponseEmnifyDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4472933252772165767L;
	
	private String dest_address;
	private String source_address;
	private int id;
	private  String submit_date;
	private  String payload;
	private  String dcs;
	private  String pid;
	private EndPoint endpoint;
	private Organization organization;
	private MultipartInfo multipartInfo;
	
	
	public EndPoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(EndPoint endpoint) {
		this.endpoint = endpoint;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public MultipartInfo getMultipartInfo() {
		return multipartInfo;
	}

	public void setMultipartInfo(MultipartInfo multipartInfo) {
		this.multipartInfo = multipartInfo;
	}

	public ResponseEmnifyDto() {
		super();
	}

	public String getDest_address() {
		return dest_address;
	}

	public void setDest_address(String dest_address) {
		this.dest_address = dest_address;
	}

	public String getSource_address() {
		return source_address;
	}

	public void setSource_address(String source_address) {
		this.source_address = source_address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubmit_date() {
		return submit_date;
	}

	public void setSubmit_date(String submit_date) {
		this.submit_date = submit_date;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getDcs() {
		return dcs;
	}

	public void setDcs(String dcs) {
		this.dcs = dcs;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}



