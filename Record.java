package com.hiveag.geepy.pojo;
// Generated May 29, 2017 4:55:35 PM by Hibernate Tools 4.3.5.Final

import java.time.LocalDateTime;
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

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.util.View;

/**
 * Record generated by hbm2java
 */
@Entity
@Table(name = "record", schema = "public")
public class Record implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3694123697470962282L;
	private RecordId id;
	private Bike bike;
	private Geepy geepy;
	private boolean reState;
	private LocalDateTime reTimestamp;

	public Record() {
	}

	public Record(RecordId id, Bike bike, Geepy geepy, boolean reState, LocalDateTime reTimestamp) {
		this.id = id;
		this.bike = bike;
		this.geepy = geepy;
		this.reState = reState;
		this.reTimestamp = reTimestamp;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "biId", column = @Column(name = "bi_id", nullable = false)),
	@AttributeOverride(name = "geImei", column = @Column(name = "ge_imei", nullable = false)) })
	@JsonView(View.Summary.class)
	public RecordId getId() {
		return this.id;
	}

	public void setId(RecordId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bi_id", nullable = false, insertable = false, updatable = false)
	@JsonView(View.Summary.class)
	public Bike getBike() {
		return this.bike;
	}

	public void setBike(Bike bike) {
		this.bike = bike;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ge_imei", nullable = false, insertable = false, updatable = false)
	@JsonView(View.Summary.class)
	public Geepy getGeepy() {
		return this.geepy;
	}

	public void setGeepy(Geepy geepy) {
		this.geepy = geepy;
	}

	@Column(name = "re_state", nullable = false)
	public boolean isReState() {
		return this.reState;
	}

	public void setReState(boolean reState) {
		this.reState = reState;
	}

	
	@Column(name = "re_timestamp", nullable = false, length = 35)
	@JsonView(View.Summary.class)
	public LocalDateTime getReTimestamp() {
		return this.reTimestamp;
	}

	public void setReTimestamp(LocalDateTime reTimestamp) {
		this.reTimestamp = reTimestamp;
	}

}
