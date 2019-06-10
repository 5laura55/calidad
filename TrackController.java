package com.hiveag.geepy.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.hiveag.geepy.dto.Body;
import com.hiveag.geepy.dto.MessageDTO;
import com.hiveag.geepy.dto.TipoNotificacionDTO;
import com.hiveag.geepy.dto.TrackDTO;
import com.hiveag.geepy.pojo.Bike;
import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.pojo.Token;
import com.hiveag.geepy.pojo.Track;
import com.hiveag.geepy.pojo.TrackId;
import com.hiveag.geepy.service.BikeService;
import com.hiveag.geepy.service.PersonService;
import com.hiveag.geepy.service.PushService;
import com.hiveag.geepy.service.TrackService;

@Controller
public class TrackController {

	@Autowired
	TrackService trackService;

	@Autowired
	BikeService bikeService;

	@Autowired
	PersonService personService;
	
	@Autowired
	PushService push;
	
	

	@Autowired
	private SimpMessagingTemplate template;

	@RequestMapping(value = "/api/track", method = RequestMethod.GET)
	@ResponseBody
	public List<Track> getAll() {

		return trackService.getAll();

	}

	@RequestMapping(value = "api/track/bike/{biId}/date/{trTimestamp}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getTrack(@PathVariable("biId") long biId, @PathVariable("trTimestamp") String str) {

		Date trTimestamp = new Date(Long.parseLong(str));
		TrackId id = new TrackId();
		id.setBiId(biId);
		id.setTrTimestamp(trTimestamp);

		MessageDTO entityMessage = new MessageDTO();
		Track track = trackService.getById(id);

		if (track == null) {
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("Track does not exist yet");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Track>(track, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/bike/{biId}/track", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("biId") long biId) {
		MessageDTO entityMessage = new MessageDTO();
		List<Track> tracks = trackService.getByBikeId(biId);

		if (tracks == null) {
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("Tracks does not exist yet");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Track>>(tracks, HttpStatus.OK);

	}

	

	@RequestMapping(value = "/api/track", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<?> createTrack(@RequestBody TrackDTO trackDTO) {
		MessageDTO entityMessage = new MessageDTO();
        Track track=trackDTO.getTrack();
        TipoNotificacionDTO tipo=new TipoNotificacionDTO();
        tipo.setTipoNotificacion("track");
        
        Bike bikeBD = bikeService.findById(track.getId().getBiId());
		if (bikeBD== null) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("Bike does not exist yet");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}
		System.out.println("TRACK POST biId:"+bikeBD.getBiId());
		Track trackBD = trackService.getById(track.getId());
		if (trackBD != null) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("Track exists already");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}

		trackDTO.setTrack(trackService.create(track));
		tipo.setBody(mapperTrackDTO(trackDTO));
		tipo.getBody().setBiStatus(bikeBD.getBiStatus());
		
		try {
		    Person person=bikeService.getPersonFromBiId(track.getId().getBiId());
		   this.template.convertAndSend("/topic/message/" + person.getPeId(), tipo);
		   System.out.println("empieza push");
      		push.sendPushTrack(person, trackDTO);
      		
//      		sendPush(track);
		
		}catch (Exception e ) {
			System.out.println(e.getMessage());
		}
		entityMessage.setMeCode(HttpStatus.CREATED);
		entityMessage.setMeMessage("Track was created");
		return new ResponseEntity<TipoNotificacionDTO>(tipo, HttpStatus.CREATED);
	}
	
	
	
	public Body mapperTrackDTO(TrackDTO trackDTO) {
		Body body =new Body();
		Track track=trackDTO.getTrack();
		body.setBattery(trackDTO.getBattery());
		body.setSignal(trackDTO.getSignal());
		body.setBiId(track.getId().getBiId());
		body.setLongitude(track.getTrLongitude());
		body.setLatitude(track.getTrLatitude());
		body.setTime_gps(track.getId().getTrTimestamp());
		return body;
	}

}
