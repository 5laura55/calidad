package com.hiveag.geepy.pojo;
// Generated May 29, 2017 4:55:35 PM by Hibernate Tools 4.3.5.Final

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Authority generated by hbm2java
 */
@Entity
@Table(name = "authority", schema = "public")
public class Authority implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6392938649365171190L;
	private AuthorityId id;
	private Person person;
	private Role role;
	private Date auTimestamp;

	public Authority() {
	}

	public Authority(AuthorityId id, Person person, Role role, Date auTimestamp) {
		this.id = id;
		this.person = person;
		this.role = role;
		this.auTimestamp = auTimestamp;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "peId", column = @Column(name = "pe_id", nullable = false)),
			@AttributeOverride(name = "roId", column = @Column(name = "ro_id", nullable = false)) })
	public AuthorityId getId() {
		return this.id;
	}

	public void setId(AuthorityId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pe_id", nullable = false, insertable = false, updatable = false)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ro_id", nullable = false, insertable = false, updatable = false)
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "au_timestamp", nullable = false, length = 35)
	public Date getAuTimestamp() {
		return this.auTimestamp;
	}

	public void setAuTimestamp(Date auTimestamp) {
		this.auTimestamp = auTimestamp;
	}

}