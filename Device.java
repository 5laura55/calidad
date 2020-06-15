package com.hiveag.geepy.pojo;
// Generated May 29, 2017 4:55:35 PM by Hibernate Tools 4.3.5.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.util.View;

/**
 * Device generated by hbm2java
 */
@Entity
@Table(name = "device", schema = "public")
public class Device implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4442944564382202373L;
	private long deId;
	private Person person;
	private String deDevId;
	private String deRegId;
	private String deAppVersion;
	private char deOs;
	private char deStatus;
	private String deOsVersion;
	private Date deLastLogon;

	public Device() {
	}

	public Device(long deId, Person person, String deDevId, String deRegId, String deAppVersion, char deOs,
			String deOsVersion, Date deLastLogon) {
		this.deId = deId;
		this.person = person;
		this.deDevId = deDevId;
		this.deRegId = deRegId;
		this.deAppVersion = deAppVersion;
		this.deOs = deOs;
		this.deOsVersion = deOsVersion;
		this.deLastLogon = deLastLogon;
	}
	
	
	public Device(long deId, String deDevId) {
		this.deId = deId;
		this.deDevId = deDevId;
	}
	
	
	
	public Device( String deDevId, String deRegId, String deAppVersion, char deOs,
			String deOsVersion, Date deLastLogon) {
		
		this.deDevId = deDevId;
		this.deRegId = deRegId;
		this.deAppVersion = deAppVersion;
		this.deOs = deOs;
		this.deOsVersion = deOsVersion;
		this.deLastLogon = deLastLogon;
	}
	@Id
    @GeneratedValue(generator="InvSeq", strategy = GenerationType.SEQUENCE) 
    @SequenceGenerator(name="InvSeq", sequenceName="device_de_id_seq", allocationSize=1) 
	@Column(name = "de_id", unique = true, nullable = false)
	@JsonView(View.Summary.class)
	public long getDeId() {
		return this.deId;
	}

	public void setDeId(long deId) {
		this.deId = deId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pe_id", nullable = false)	
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "de_dev_id", nullable = false, length = 36)
	@JsonView(View.Summary.class)
	public String getDeDevId() {
		return this.deDevId;
	}

	public void setDeDevId(String deDevId) {
		this.deDevId = deDevId;
	}

	@Column(name = "de_reg_id", nullable = false)
	@JsonView(View.Summary.class)
	public String getDeRegId() {
		return this.deRegId;
	}

	public void setDeRegId(String deRegId) {
		this.deRegId = deRegId;
	}

	@Column(name = "de_app_version", nullable = false, length = 10)
	@JsonView(View.Summary.class)
	public String getDeAppVersion() {
		return this.deAppVersion;
	}

	public void setDeAppVersion(String deAppVersion) {
		this.deAppVersion = deAppVersion;
	}
	
	@Column(name = "de_os", nullable = false, length = 1)
	@JsonView(View.Summary.class)
	public char getDeOs() {
		return this.deOs;
	}

	public void setDeOs(char deOs) {
		this.deOs = deOs;
	}
	
	@Column(name = "de_status",  length = 1)
	@JsonView(View.Summary.class)
	public char getDeStatus() {
		return this.deStatus;
	}

	public void setDeStatus(char deStatus) {
		this.deStatus = deStatus;
	}

	@Column(name = "de_os_version", nullable = false, length = 10)
	@JsonView(View.Summary.class)
	public String getDeOsVersion() {
		return this.deOsVersion;
	}

	public void setDeOsVersion(String deOsVersion) {
		this.deOsVersion = deOsVersion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "de_last_logon", nullable = false, length = 35)
	@JsonView(View.Summary.class)
	public Date getDeLastLogon() {
		return this.deLastLogon;
	}

	public void setDeLastLogon(Date deLastLogon) {
		this.deLastLogon = deLastLogon;
	}

}