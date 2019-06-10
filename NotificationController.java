package com.hiveag.geepy.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hiveag.geepy.dao.AbstractDAO;
import com.hiveag.geepy.dto.Body;
import com.hiveag.geepy.dto.MessageDTO;
import com.hiveag.geepy.dto.NotificationDTO;
import com.hiveag.geepy.dto.TipoNotificacionDTO;
import com.hiveag.geepy.pojo.Geepy;
import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.pojo.Record;
import com.hiveag.geepy.service.BikeService;
import com.hiveag.geepy.service.EmnifyService;
import com.hiveag.geepy.service.GeepyService;
import com.hiveag.geepy.service.PushService;
import com.hiveag.geepy.service.RecordService;

@Controller
public class NotificationController {
	protected static final Log log = LogFactory.getLog(AbstractDAO.class);

	@Autowired
	BikeService bikeService;

	@Autowired
	RecordService recordService;

	@Autowired
	GeepyService geepyService;

	@Autowired
	PushService push;

	@Autowired
	EmnifyService emnifyService;

	@Autowired
	private SimpMessagingTemplate template;

	@RequestMapping(value = "/api/notification", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> createTrack(@RequestBody NotificationDTO notificationDTO) {
		MessageDTO entityMessage = new MessageDTO();

		TipoNotificacionDTO tipo = new TipoNotificacionDTO();
		Body body = new Body();
		tipo.setTipoNotificacion("notification");
		tipo.setBody(body);
		String message;

		Person person = bikeService.getPersonFromBiId(notificationDTO.getBiId());
		body.setBiId(notificationDTO.getBiId());

		if (notificationDTO.getCode() == 100) {
			message = "Bicicleta fuera de geocerca";
			body.setMessage(message);
			body.setCode(100);
			notificationDTO.setMessage(message);
			Record record = recordService.findByBiId(notificationDTO.getBiId());
			emnifyService.requestLocationEveryTenSeconds(record.getGeepy().getGeEmnifyId());
		}
		if (notificationDTO.getCode() == 101) {
			message = "Bateria agotandose";
			body.setCode(101);
			body.setMessage(message);
			notificationDTO.setMessage(message);
		}
		if (notificationDTO.getCode() == 102) {
			message = " Panic Alarm ";
			body.setCode(notificationDTO.getCode());
			body.setMessage(message);
			notificationDTO.setMessage(message);
		}
		try {
			this.template.convertAndSend("/topic/message/" + person.getPeId(), tipo);
		} catch (Exception e) {
			log.debug("  error web socket  ");
			return new ResponseEntity<NotificationDTO>(notificationDTO, HttpStatus.CONFLICT);
		}
		try {
			push.sendPushNotification(person, notificationDTO);
		} catch (Exception e) {
			log.debug("  error push notification ");
			return new ResponseEntity<NotificationDTO>(notificationDTO, HttpStatus.CONFLICT);
		}
		
		entityMessage.setMeCode(HttpStatus.OK);
		entityMessage.setMeMessage("new notification ");
		return new ResponseEntity<NotificationDTO>(notificationDTO, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/event", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> createEvent(@RequestBody NotificationDTO notificationDTO) {
		MessageDTO entityMessage = new MessageDTO();

		if (notificationDTO.getCode() == 21) {
			Record record = recordService.findByBiId(notificationDTO.getBiId());
			Geepy geepy = record.getGeepy();
			geepy.setGeState('E');
			geepyService.update(geepy);
		}
		System.out.println("Event code:    " + notificationDTO.getCode() + " biId " + notificationDTO.getBiId()
				+ "message" + notificationDTO.getMessage());

		return new ResponseEntity<NotificationDTO>(notificationDTO, HttpStatus.OK);

	}

}
