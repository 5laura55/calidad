package com.hiveag.geepy.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.util.View;

public class PictureDTO {
	
	
	
	private Long biId;
	private MultipartFile file;
	private short piSlot;

	
	

	public PictureDTO() {}
	
	
	
	public PictureDTO(Long biId, MultipartFile file,short piSlot) {
		super();
		
		this.biId = biId;
		this.file = file;
		this.piSlot=piSlot;
	}

	@JsonView(View.Summary.class)
	public Long getBiId() {
		return biId;
	}

	public void setBiId(Long biId) {
		this.biId = biId;
	}
	@JsonView(View.Summary.class)
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}


	


	@JsonView(View.Summary.class)
	public short getPiSlot() {
		return piSlot;
	}



	public void setPiSlot(short piSlot) {
		this.piSlot = piSlot;
	}
	
	
	
	
}
