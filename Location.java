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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.util.View;

/**
 * Location generated by hbm2java
 */
@Entity
@Table(name = "location", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "lo_alpha"))
public class Location implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -396641421432858988L;
	private int loId;
	private Location location;
	private String loName;
	private String loAlpha;
	private String loCode;
	private char loType;
	private Set<Person> persons = new HashSet<Person>(0);
	private Set<Location> locations = new HashSet<Location>(0);

	public Location() {
	}

	public Location(int loId, String loName, String loAlpha, String loCode, char loType) {
		this.loId = loId;
		this.loName = loName;
		this.loAlpha = loAlpha;
		this.loCode = loCode;
		this.loType = loType;
	}

	public Location(int loId, Location location, String loName, String loAlpha, String loCode, char loType,
			Set<Person> persons, Set<Location> locations) {
		this.loId = loId;
		this.location = location;
		this.loName = loName;
		this.loAlpha = loAlpha;
		this.loCode = loCode;
		this.loType = loType;
		this.persons = persons;
		this.locations = locations;
	}

	@Id
	@Column(name = "lo_id", unique = true, nullable = false)
	@JsonView(View.Summary.class)
	public int getLoId() {
		return this.loId;
	}

	public void setLoId(int loId) {
		this.loId = loId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lo_parent_id")
	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Column(name = "lo_name", nullable = false, length = 68)
	@JsonView(View.Summary.class)
	public String getLoName() {
		return this.loName;
	}

	public void setLoName(String loName) {
		this.loName = loName;
	}

	@Column(name = "lo_alpha", unique = true, nullable = false, length = 2)
	@JsonView(View.Summary.class)
	public String getLoAlpha() {
		return this.loAlpha;
	}

	public void setLoAlpha(String loAlpha) {
		this.loAlpha = loAlpha;
	}

	@Column(name = "lo_code", nullable = false, length = 7)
	@JsonView(View.Summary.class)
	public String getLoCode() {
		return this.loCode;
	}

	public void setLoCode(String loCode) {
		this.loCode = loCode;
	}

	@Column(name = "lo_type", nullable = false, length = 1)
	public char getLoType() {
		return this.loType;
	}

	public void setLoType(char loType) {
		this.loType = loType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
	@JsonIgnore
	public Set<Person> getPersons() {
		return this.persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
	public Set<Location> getLocations() {
		return this.locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

}
