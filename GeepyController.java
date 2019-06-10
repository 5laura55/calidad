package com.hiveag.geepy.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.dto.MessageDTO;
import com.hiveag.geepy.dto.ResponseEmnifyDto;
import com.hiveag.geepy.pojo.Bike;
import com.hiveag.geepy.pojo.Geepy;
import com.hiveag.geepy.pojo.Message;
import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.pojo.Record;
import com.hiveag.geepy.pojo.Token;
import com.hiveag.geepy.service.BikeService;
import com.hiveag.geepy.service.GeepyService;
import com.hiveag.geepy.service.PersonService;
import com.hiveag.geepy.service.RecordService;
import com.hiveag.geepy.util.CustomUserChecker;
import com.hiveag.geepy.util.View;

@RestController
@SessionAttributes("theToken")
public class GeepyController {

	@Autowired
	private GeepyService geepyService;

	private Double latitud = 5.536901;
	private Double longitud = -73.361907;

	@Autowired
	private RecordService recordService;

	@Autowired
	private BikeService bikeService;

	@Autowired
	private SimpMessagingTemplate template;

	@RequestMapping(value = "/api/geepy", method = RequestMethod.GET, produces = "application/json")
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getAll() {
		List<Geepy> listGeepy = geepyService.getAll();
		MessageDTO entityMessage = new MessageDTO();
		if (listGeepy == null) {
			entityMessage.setMeCode(HttpStatus.NO_CONTENT);
			entityMessage.setMeMessage("There are not geepies in the system");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Geepy>>(listGeepy, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/geepy/{geImei}", method = RequestMethod.GET, produces = "application/json")
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getGeepyByImei(@PathVariable("geImei") long geImei) {
		Geepy geepy = geepyService.findById(geImei);
		MessageDTO entityMessage = new MessageDTO();
		if (geepy == null) {
			entityMessage.setMeCode(HttpStatus.NO_CONTENT);
			entityMessage.setMeMessage("There are not geepies in the system");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Geepy>(geepy, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/api/geepy/validate/{geImei}", method = RequestMethod.GET, produces = "application/json")
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getGeepyByImeiValidate(@PathVariable("geImei") long geImei) {
		Geepy geepy = geepyService.findById(geImei);
		MessageDTO entityMessage = new MessageDTO();
		if (geepy == null) {
			entityMessage.setMeCode(HttpStatus.NO_CONTENT);
			entityMessage.setMeMessage("There are not geepies in the system");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NO_CONTENT);
		}
		if(geepy.getPerson()!=null) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
		    entityMessage.setMeMessage("El geepy ya esta asignado a una persona ");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Geepy>(geepy, HttpStatus.OK);
	}
	
	

	@RequestMapping(value = "/api/geepy", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> create(@Valid @RequestBody Geepy geepy) {
		MessageDTO entityMessage = new MessageDTO();
		geepyService.create(geepy);
		entityMessage.setMeCode(HttpStatus.CREATED);
		entityMessage.setMeMessage("Geepy was created");
		return new ResponseEntity<Geepy>(geepy, HttpStatus.CREATED);
	}

		

}
