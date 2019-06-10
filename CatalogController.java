package com.hiveag.geepy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.dto.MessageDTO;
import com.hiveag.geepy.pojo.Catalog;
import com.hiveag.geepy.service.CatalogService;
import com.hiveag.geepy.util.View;


@RestController
public class CatalogController {

	@Autowired
	CatalogService catalogService;
	
	
	@RequestMapping(value = "/api/catalog", method = RequestMethod.GET, produces = "application/json")
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getBrands() {

		List<Catalog> brands= catalogService.getBrands();
		MessageDTO entityMessage = new MessageDTO();

		if (brands == null) {
			entityMessage.setMeCode(HttpStatus.NO_CONTENT);
			entityMessage.setMeMessage("There are not brands in the system");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Catalog>>(brands, HttpStatus.OK);
	}	
	
	
	@RequestMapping(value = "/api/catalog/{caId}", method = RequestMethod.GET, produces = "application/json")
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getTypes(@PathVariable("caId") int caId) {
		List<Catalog> models= catalogService.getChilds(caId);
		MessageDTO entityMessage = new MessageDTO();
		if (models == null) {
			entityMessage.setMeCode(HttpStatus.NO_CONTENT);
			entityMessage.setMeMessage("There are not categories  in the system");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Catalog>>(models, HttpStatus.OK);
	}	
	
	
	@RequestMapping(value = "/api/catalog/{caId}/parents", method = RequestMethod.GET, produces = "application/json")
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getParents(@PathVariable("caId") int caId) {
		List<Catalog> parents= catalogService.getParents(caId);
		MessageDTO entityMessage = new MessageDTO();
		if (parents == null) {
			entityMessage.setMeCode(HttpStatus.NO_CONTENT);
			entityMessage.setMeMessage("There are not categories  in the system");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Catalog>>(parents, HttpStatus.OK);
	}	
	
	

	

}