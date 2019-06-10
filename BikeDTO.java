package com.hiveag.geepy.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.util.View;

public class BikeDTO implements java.io.Serializable {

	private static final long serialVersionUID = -4577429355064763313L;

	private String biId;
	private String biHexColor;
	private String biSerial;
	private String biYear;
	private String biStatus;
	private String biGeoStatus;
	private String biGeoRadius;
	private String piPath;
	private String caId;
	private String caName;
	private String caType;
	private String geImei;
	private String geSerial;
	private String geImsi;
	private String geMsisdn;
	private String geState;
	
	@JsonView(View.Summary.class)
	public String getBiId() {
		return biId;
	}
	public void setBiId(String biId) {
		this.biId = biId;
	}
	
	@JsonView(View.Summary.class)
	public String getBiHexColor() {
		return biHexColor;
	}
	public void setBiHexColor(String biHexColor) {
		this.biHexColor = biHexColor;
	
	}
	
	@JsonView(View.Summary.class)
	public String getBiSerial() {
		return biSerial;
	}
	public void setBiSerial(String biSerial) {
		this.biSerial = biSerial;
	}
	
	@JsonView(View.Summary.class)
	public String getBiYear() {
		return biYear;
	}
	public void setBiYear(String biYear) {
		this.biYear = biYear;
	}
	
	@JsonView(View.Summary.class)
	public String getBiStatus() {
		return biStatus;
	}
	public void setBiStatus(String biStatus) {
		this.biStatus = biStatus;
	}
	
	@JsonView(View.Summary.class)
	public String getPiPath() {
		return piPath;
	}
	public void setPiPath(String piPath) {
		this.piPath = piPath;
	}
	
	@JsonView(View.Summary.class)
	public String getCaId() {
		return caId;
	}
	public void setCaId(String caId) {
		this.caId = caId;
	}
	
	@JsonView(View.Summary.class)
	public String getCaName() {
		return caName;
	}
	public void setCaName(String caName) {
		this.caName = caName;
	
	}
	
	@JsonView(View.Summary.class)
	public String getCaType() {
		return caType;
	}
	public void setCaType(String caType) {
		this.caType = caType;
	}
	
	@JsonView(View.Summary.class)
	public String getGeImei() {
		return geImei;
	}
	public void setGeImei(String geImei) {
		this.geImei = geImei;
	}
	
	@JsonView(View.Summary.class)
	public String getGeSerial() {
		return geSerial;
	}
	public void setGeSerial(String geSerial) {
		this.geSerial = geSerial;
	}
	
	@JsonView(View.Summary.class)
	public String getGeImsi() {
		return geImsi;
	}
	public void setGeImsi(String geImsi) {
		this.geImsi = geImsi;
	}
	@JsonView(View.Summary.class)
	public String getGeMsisdn() {
		return geMsisdn;
	}
	public void setGeMsisdn(String geMsisdn) {
		this.geMsisdn = geMsisdn;
	}
	
	@JsonView(View.Summary.class)
	public String getGeState() {
		return geState;
	}
	public void setGeState(String geState) {
		this.geState = geState;
	}
	
	
	
	
	
	@JsonView(View.Summary.class)
	public String getBiGeoStatus() {
		return biGeoStatus;
	}
	public void setBiGeoStatus(String biGeoStatus) {
		this.biGeoStatus = biGeoStatus;
	}
	@JsonView(View.Summary.class)
	public String getBiGeoRadius() {
		return biGeoRadius;
	}
	public void setBiGeoRadius(String biGeoRadius) {
		this.biGeoRadius = biGeoRadius;
	}
	public BikeDTO() {
		
	}
	public BikeDTO(String biId, String biHexColor, String biSerial, String biYear, String biStatus, String biGeoStatus,
			String biGeoRadius, String piPath, String caId, String caName, String caType, String geImei,
			String geSerial, String geImsi, String geMsisdn, String geState) {
		super();
		this.biId = biId;
		this.biHexColor = biHexColor;
		this.biSerial = biSerial;
		this.biYear = biYear;
		this.biStatus = biStatus;
		this.biGeoStatus = biGeoStatus;
		this.biGeoRadius = biGeoRadius;
		this.piPath = piPath;
		this.caId = caId;
		this.caName = caName;
		this.caType = caType;
		this.geImei = geImei;
		this.geSerial = geSerial;
		this.geImsi = geImsi;
		this.geMsisdn = geMsisdn;
		this.geState = geState;
	}
	

	
	
	
	
	
	

}
