package com.hiveag.geepy.controller;


import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.dao.AbstractDAO;
import com.hiveag.geepy.dto.BikeDTO;
import com.hiveag.geepy.dto.Body;
import com.hiveag.geepy.dto.MessageDTO;
import com.hiveag.geepy.exception.ResourceNotFoundException;
import com.hiveag.geepy.pojo.Bike;
import com.hiveag.geepy.pojo.Picture;
import com.hiveag.geepy.pojo.Record;
import com.hiveag.geepy.service.BikeService;
import com.hiveag.geepy.service.RecordService;
import com.hiveag.geepy.util.View;

@RestController
public class BikeController {
	
	protected static final Log log = LogFactory.getLog(AbstractDAO.class);

	@Autowired
	BikeService bikeService;

	@Autowired
	RecordService recordService;

	@RequestMapping(value = "/api/bike", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<MessageDTO> update(@RequestBody Bike bike) {
		MessageDTO entityMessage = new MessageDTO();
		if (bikeService.existBike(bike)) {
			bikeService.update(bike);
			entityMessage.setMeCode(HttpStatus.OK);
			entityMessage.setMeMessage("Bike  was updated");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);
		}

		entityMessage.setMeCode(HttpStatus.NOT_FOUND);
		entityMessage.setMeMessage("Bike does not exist yet");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/api/bike/geofence", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<MessageDTO> updateGeoFence(@RequestBody Bike bike) {
		MessageDTO entityMessage = new MessageDTO();

		if (!bikeService.existBike(bike)) {
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("Bike does not exist yet");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
		}

		if (bike.getBiGeoRadius() != null) {
			if (bike.getBiGeoRadius() < 50) {
				entityMessage.setMeCode(HttpStatus.CONFLICT);
				entityMessage.setMeMessage("el radio del geocerca no debe ser menor a 50 metros ");
				return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
			}
		}
		log.debug("Geocerca  "+bike.getBiId());
		bikeService.updateGeofence(bike);
		entityMessage.setMeCode(HttpStatus.OK);
		entityMessage.setMeMessage("geofence was updated");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/bike/{biId}", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<MessageDTO> delete(@PathVariable("biId") long biId) {
		Bike bike = new Bike();
		bike.setBiId(biId);

		MessageDTO entityMessage = new MessageDTO();
		if (bikeService.existBike(bike)) {
			bikeService.delete(bike);
			entityMessage.setMeCode(HttpStatus.OK);
			entityMessage.setMeMessage("Bike  was deleted");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);
		}
		entityMessage.setMeCode(HttpStatus.NOT_FOUND);
		entityMessage.setMeMessage("Bike does not exist yet");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/api/bike/serial/{biSerial}", method = RequestMethod.GET)
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getbySerial(@PathVariable("biSerial") String biSerial) {
		Bike bike = bikeService.findBySerial(biSerial);
		Record record = recordService.findByBiId(bike.getBiId());
		if (record == null) {
			throw new ResourceNotFoundException(1L);
		}
		return new ResponseEntity<Record>(record, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/bike/id/{biId}", method = RequestMethod.GET)
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getById2(@PathVariable("biId") long biId) {
		Record record = recordService.findByBiId(biId);
		if (record == null) {
			throw new ResourceNotFoundException(biId);
		}
		return new ResponseEntity<Record>(record, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/{peId}/bikes", method = RequestMethod.GET, produces = "application/json")
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getBikesPerson(@PathVariable("peId") Long peId) {
		MessageDTO entityMessage = new MessageDTO();

		List<BikeDTO> bikes = bikeService.bikesPerson(peId);
		if (bikes == null) {
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("User does not exist yet");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<BikeDTO>>(bikes, HttpStatus.OK);

	}



	@RequestMapping(value = "/api/bike/{biId}/location", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> getLocation(@PathVariable("biId") long biId) {
		MessageDTO entityMessage = new MessageDTO();

		Record record = recordService.findByBiId(biId);

		if (record.getGeepy().getGeEmnifyId() == null) {
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("error register geepy bike");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
		}

		bikeService.locationBike(record);
		entityMessage.setMeCode(HttpStatus.OK);
		entityMessage.setMeMessage("locating  bike");

		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/bike/{biId}/lastlocation", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> getLastLocation(@PathVariable("biId") long biId) {
		MessageDTO entityMessage = new MessageDTO();

		try {
			Record record = recordService.findByBiId(biId);
			Body geepyDTO = bikeService.getLastLocation(record);
			return new ResponseEntity<Body>(geepyDTO, HttpStatus.OK);
			
		} catch (Exception e) {
			Body  geepyDTO=new Body();
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("not found geepy");
			return new ResponseEntity<Body>(geepyDTO, HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = "/api/bike/{biId}/lost", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity reportBikeLostOrStolen(@PathVariable("biId") long biId) {
		MessageDTO entityMessage = new MessageDTO();

		Bike bike = bikeService.findById(biId);

		if (bike == null) {
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("bike does not exist yet");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
		}

		bikeService.lostBike(bike);
         
		entityMessage.setMeCode(HttpStatus.NOT_FOUND);
		entityMessage.setMeMessage("update bike state");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);
		

	}

}
