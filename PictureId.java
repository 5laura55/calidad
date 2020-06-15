package com.hiveag.geepy.pojo;
// Generated May 29, 2017 4:55:35 PM by Hibernate Tools 4.3.5.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.util.View;

/**
 * PictureId generated by hbm2java
 */
@Embeddable
public class PictureId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5170563533803816038L;
	private long biId;
	private short piSlot;

	public PictureId() {
	}

	public PictureId(long biId, short piSlot) {
		this.biId = biId;
		this.piSlot = piSlot;
	}

	@Column(name = "bi_id", nullable = false)
	@JsonView(View.Summary.class)
	public long getBiId() {
		return this.biId;
	}

	public void setBiId(long biId) {
		this.biId = biId;
	}

	@Column(name = "pi_slot", nullable = false)
	@JsonView(View.Summary.class)
	public short getPiSlot() {
		return this.piSlot;
	}

	public void setPiSlot(short piSlot) {
		this.piSlot = piSlot;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PictureId))
			return false;
		PictureId castOther = (PictureId) other;

		return (this.getBiId() == castOther.getBiId()) && (this.getPiSlot() == castOther.getPiSlot());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getBiId();
		result = 37 * result + this.getPiSlot();
		return result;
	}

}