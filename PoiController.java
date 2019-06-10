package com.hiveag.geepy.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.dto.BikeDTO;
import com.hiveag.geepy.dto.MessageDTO;
import com.hiveag.geepy.dto.PoiLimitDTO;
import com.hiveag.geepy.exception.InvalidRequestException;
import com.hiveag.geepy.exception.ResourceNotFoundException;
import com.hiveag.geepy.pojo.Bike;
import com.hiveag.geepy.pojo.Poi;
import com.hiveag.geepy.pojo.Record;
import com.hiveag.geepy.service.PoiService;
import com.hiveag.geepy.util.View;

@RestController
public class PoiController {

	@Autowired
	PoiService poiService;
	
	

	@RequestMapping(value = "/api/poi", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> create( @Valid  @RequestBody Poi poi, BindingResult result) {
		MessageDTO entityMessage = new MessageDTO();
		poi=poiService.create(poi);
		entityMessage.setMeCode(HttpStatus.CREATED);
		entityMessage.setMeMessage("Interest Point  was created");
		return new ResponseEntity<Poi>(poi, HttpStatus.CREATED);

	}
	
	@RequestMapping(value = "/api/poi/{poId}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<?> delete( @PathVariable("poId") long poId) {
		MessageDTO entityMessage = new MessageDTO();
		
		
		
		Poi poi=poiService.get(poId);
		
		
		if(poi==null)
		{  entityMessage.setMeCode(HttpStatus.CONFLICT);
		  entityMessage.setMeMessage("EL Punto de interes no existe");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}
		
		if(poiService.delete(poi))
		{  entityMessage.setMeCode(HttpStatus.OK);
		  entityMessage.setMeMessage("Interest Point  was delete");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);
		}
		
		entityMessage.setMeCode(HttpStatus.CONFLICT);
		 entityMessage.setMeMessage("error delete");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
	}
	
	
	@RequestMapping(value = "/api/poi/{poId}", method = RequestMethod.GET)
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("poId") long poId) {
		Poi poi= poiService.get(poId);
	
		if(poi == null) {
            throw new ResourceNotFoundException( 1L);
        }
		return new ResponseEntity<Poi>(poi, HttpStatus.OK);

	}
	
	
	
	
	
	
	@RequestMapping(value = "/api/poi/limits", method = RequestMethod.POST)
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> get(@RequestBody PoiLimitDTO limit) {
      MessageDTO entityMessage = new MessageDTO();
		
      List<Poi>points= poiService.getPoints(limit);
		
		
		
		if (points == null) {
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("points does not exist ");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity< List<Poi>>(points, HttpStatus.OK);

	}
	
	

}
