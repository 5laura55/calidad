package com.hiveag.geepy.pojo;
// Generated May 29, 2017 4:55:35 PM by Hibernate Tools 4.3.5.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.util.View;

/**
 * Geepy generated by hbm2java
 */
@Entity
@Table(name = "geepy", schema = "public", uniqueConstraints = { @UniqueConstraint(columnNames = "ge_imsi"),
		@UniqueConstraint(columnNames = "ge_msisdn"), @UniqueConstraint(columnNames = "ge_serial") })
public class Geepy implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5298196007221360714L;
	private long geImei;
	private Long geEmnifyId;
	private Person person;
	private String geSerial;
	private long geImsi;
	private long geMsisdn;
	private char geState;
	private Set<Record> records = new HashSet<Record>(0);

	public Geepy() {
	}

	public Geepy(long geImei, String geSerial, long geImsi, int geMsisdn, char geState) {
		this.geImei = geImei;
		this.geSerial = geSerial;
		this.geImsi = geImsi;
		this.geMsisdn = geMsisdn;
		this.geState = geState;
	}

	public Geepy(long geImei, Person person, String geSerial, long geImsi, int geMsisdn, char geState,
			Set<Record> records) {
		this.geImei = geImei;
		this.person = person;
		this.geSerial = geSerial;
		this.geImsi = geImsi;
		this.geMsisdn = geMsisdn;
		this.geState = geState;
		this.records = records;
	}

	@Id
	@Column(name = "ge_imei", unique = true, nullable = false)
	@JsonView(View.Summary.class)
	public long getGeImei() {
		return this.geImei;
	}

	public void setGeImei(long geImei) {
		this.geImei = geImei;
	}
	
	
	
		
	
	
	@Column(name = "ge_emnify_id", unique = true)
	@JsonView(View.Summary.class)
	public Long getGeEmnifyId() {
		return geEmnifyId;
	}

	public void setGeEmnifyId(Long geEmnifyId) {
		this.geEmnifyId = geEmnifyId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pe_id")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "ge_serial", unique = true, nullable = false, length = 10)
	@JsonView(View.Summary.class)
	
	public String getGeSerial() {
		return this.geSerial;
	}

	public void setGeSerial(String geSerial) {
		this.geSerial = geSerial;
	}

	@Column(name = "ge_imsi", unique = true, nullable = false)
	@JsonView(View.Summary.class)
	public long getGeImsi() {
		return this.geImsi;
	}

	public void setGeImsi(long geImsi) {
		this.geImsi = geImsi;
	}

	@Column(name = "ge_msisdn", unique = true, nullable = false)
	@JsonView(View.Summary.class)
	public long getGeMsisdn() {
		return this.geMsisdn;
	}

	public void setGeMsisdn(long geMsisdn) {
		this.geMsisdn = geMsisdn;
	}

	@Column(name = "ge_state", nullable = false, length = 1)
	@JsonView(View.Summary.class)
	public char getGeState() {
		return this.geState;
	}

	public void setGeState(char geState) {
		this.geState = geState;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "geepy")
	public Set<Record> getRecords() {
		return this.records;
	}

	public void setRecords(Set<Record> records) {
		this.records = records;
	}

}