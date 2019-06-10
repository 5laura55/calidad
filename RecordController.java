package com.hiveag.geepy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hiveag.geepy.dto.MessageDTO;
import com.hiveag.geepy.pojo.Bike;
import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.pojo.Record;
import com.hiveag.geepy.service.BikeService;
import com.hiveag.geepy.service.GeepyService;
import com.hiveag.geepy.service.RecordService;
import com.hiveag.geepy.util.CustomUserChecker;

@RestController
public class RecordController {

	@Autowired
	RecordService recordService;

	@Autowired
	GeepyService geepyService;
	

	@Autowired
	BikeService bikeService;
	
	
	@RequestMapping(value = "/api/record", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> assignBikePerson(@RequestBody Record record,OAuth2Authentication authentication)  {
		MessageDTO entityMessage = new MessageDTO();
		long peId = new CustomUserChecker().checker(authentication);
		Person person=new Person();
		person.setPeId(peId);
		record.getGeepy().setPerson(person);
		
		if(geepyService.geepyAsignado(record.getGeepy()))
		{   entityMessage.setMeCode(HttpStatus.CONFLICT);
		    entityMessage.setMeMessage("El geepy ya esta asignado a una persona ");
		    return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}
		
		Bike bike=bikeService.findBySerial(record.getBike().getBiSerial());
		if(bike!=null)
		{   entityMessage.setMeCode(HttpStatus.CONFLICT);
		    entityMessage.setMeMessage("el serial " + record.getBike().getBiSerial()+"  ya se encuentra registrado");
		    return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}
		
	 	record =recordService.create(record); 
		entityMessage.setMeCode(HttpStatus.CREATED);
		entityMessage.setMeMessage("record  was created");
		return new ResponseEntity<Record>(record, HttpStatus.CREATED);
		

	}
	

}
