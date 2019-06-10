package com.hiveag.geepy.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.dto.MessageDTO;
import com.hiveag.geepy.pojo.Device;
import com.hiveag.geepy.pojo.Geepy;
import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.pojo.Track;
import com.hiveag.geepy.service.DeviceService;
import com.hiveag.geepy.util.CustomUserChecker;
import com.hiveag.geepy.util.View;

@Controller
public class DeviceController {
	@Autowired
	DeviceService deviceService;

	@RequestMapping(value = "/api/device/{peId}", method = RequestMethod.GET, produces = "application/json")
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getPeId(@PathVariable("peId") long peId) {
		MessageDTO entityMessage = new MessageDTO();
		List<Device> devices = deviceService.findByIdPerson(peId);
		if (devices == null) {
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("Device does not exist yet");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<Device>>(devices, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/device/{deId}", method = RequestMethod.GET, produces = "application/json")
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getDeId(@PathVariable("deId") long deId) {
		MessageDTO entityMessage = new MessageDTO();
		Device devices = deviceService.findByIdDevice(deId);
		if (devices == null) {
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("Device does not exist yet");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Device>(devices, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/device", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody Device device, OAuth2Authentication authentication) {
		MessageDTO entityMessage = new MessageDTO();
		long peId = new CustomUserChecker().checker(authentication);
		device.getPerson().setPeId(peId);
		device.setDeLastLogon(new Date());

		deviceService.create(device);
		entityMessage.setMeCode(HttpStatus.CREATED);
		entityMessage.setMeMessage("Device was created");
		return new ResponseEntity<Device>(device, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/api/device/{deId}/regid", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<?> updateRegId(@PathVariable("deId") long deId) {
		MessageDTO entityMessage = new MessageDTO();
		Device deviceBD = deviceService.findByIdDevice(deId);

		if (deviceBD == null) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("device not exists ");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}

		deviceBD=deviceService.updateRegId(deviceBD);
		entityMessage.setMeCode(HttpStatus.OK);
		entityMessage.setMeMessage("Device reg_id was delete");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);
		
	}
	
	
	@RequestMapping(value = "/api/device/status", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<?> updateStatus(@RequestBody Device device, OAuth2Authentication authentication)  {
		MessageDTO entityMessage = new MessageDTO();
		Device deviceBD = deviceService.findByIdDevice(device.getDeId());

		if (deviceBD == null) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("device not exists ");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}

		deviceBD=deviceService.updateStatus(device);
		entityMessage.setMeCode(HttpStatus.OK);
		entityMessage.setMeMessage(" status Device update");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);
		
	}

}