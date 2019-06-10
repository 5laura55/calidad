package com.hiveag.geepy.service;


import java.sql.Timestamp;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hiveag.geepy.dto.NotificationDTO;
import com.hiveag.geepy.dto.TrackDTO;
import com.hiveag.geepy.pojo.Bike;
import com.hiveag.geepy.pojo.Device;
import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.pojo.Track;

@Service
public class PushService {

	
	private  static final Logger logger = LoggerFactory.getLogger(PushService.class);
	
	@Autowired
	private DeviceService deviceService;

	@Autowired
	private BikeService bikeService;

	public PushService() {
	}

	public void sendPushTrack(Person person, TrackDTO trackDTO) {
		List<Device> devices = deviceService.findDevicesLogInForeGround(person.getPeId());
		System.out.println("device activos "+devices.size());
		if (devices.size() > 0) {

			JSONArray registrationIds = new JSONArray();
			for (Device device : devices) {
				registrationIds.put(device.getDeRegId());
			}
			pushTrack(trackDTO, registrationIds);
		}
	}

	
	public void sendPushNotification(Person person, NotificationDTO notificationDTO) {
		Bike bike = bikeService.findById(notificationDTO.getBiId());
		List<Device> devices = deviceService.findDevicesLogIn(person.getPeId());
		if (devices.size() > 0) {
			JSONArray registrationIds = new JSONArray();
			for (Device device : devices) {
				registrationIds.put(device.getDeRegId());
			}
			pushNotification(notificationDTO, bike, registrationIds);
		}
	}

	public String pushNotification(NotificationDTO notificationDTO, Bike bike, JSONArray registrationIds) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("https://fcm.googleapis.com/fcm/send");
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Authorization", "key=AIzaSyD3ISHsFMQkhIguFUQ98zipDGkzpgSYJ-I");

		JSONObject contenido = new JSONObject();
		JSONObject noti = new JSONObject();
		JSONObject data = new JSONObject();

		noti.put("title", notificationDTO.getMessage());
		noti.put("text", notificationDTO.getMessage());
		noti.put("badge", "1");
		noti.put("sound", "default");

		data.put("title", notificationDTO.getMessage());
		data.put("message", notificationDTO.getMessage());
		data.put("biId", notificationDTO.getBiId());
		data.put("biSerial", bike.getBiSerial());
		data.put("dateTimestamp", timestamp.getTime());
		data.put("codeMessage", notificationDTO.getCode());

		contenido.put("notification", noti);
		contenido.put("data", data);
		contenido.put("content_available", true);
		contenido.put("priority", "high");
		contenido.put("registration_ids", registrationIds);

		StringEntity stringEntity;
		try {
			stringEntity = new StringEntity(contenido.toString());
			logger.info("Sending push Notification");
			logger.info(contenido.toString());

			httpPost.setEntity(stringEntity);
			HttpResponse response = httpClient.execute(httpPost);
			logger.info("RESPUESTA FIREBASE    " + response);
			String rspstr = EntityUtils.toString(response.getEntity());
			return rspstr;

		} catch (Exception e) {
			logger.info("error push notification " );
			
		} 
		return contenido.toString();
	}

	public String pushTrack(TrackDTO trackDTO, JSONArray registrationIds) {
		System.out.println("RESPUESTA FIREBASE    " );
		Track track = trackDTO.getTrack();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("https://fcm.googleapis.com/fcm/send");
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Authorization", "key=AIzaSyD3ISHsFMQkhIguFUQ98zipDGkzpgSYJ-I");

		JSONObject contenido = new JSONObject();
		JSONObject data = new JSONObject();

		data.put("title", "point");
		data.put("message", "point");
		data.put("biId", track.getId().getBiId());
		data.put("latitude", track.getTrLatitude());
		data.put("longitude", track.getTrLongitude());
		data.put("battery", trackDTO.getBattery());
		data.put("signal", trackDTO.getSignal());

		contenido.put("data", data);
		contenido.put("content_available", true);
		contenido.put("priority", "high");
		contenido.put("registration_ids", registrationIds);

		StringEntity stringEntity;
		try {
			stringEntity = new StringEntity(contenido.toString());
			logger.info("Sending push track");
			logger.info(contenido.toString());

			httpPost.setEntity(stringEntity);
			HttpResponse response = httpClient.execute(httpPost);
			logger.info("RESPUESTA FIREBASE    " + response);
			String rspstr = EntityUtils.toString(response.getEntity());

			return rspstr;
			// jrps = new JSONObject(rspstr);
		} catch (Exception e) {
			logger.info(" error send push track");
		} 
		return contenido.toString();
	}

}
