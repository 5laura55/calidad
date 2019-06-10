package com.hiveag.geepy.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hiveag.geepy.pojo.Token;

@Service
public class EmnifyService {

	private  static final Logger logger = LoggerFactory.getLogger(EmnifyService.class);

	/* token de emnify */
	public Token authentication() {

		String username = "jorge@m2mtelecom.com";
		String password = "dc51e76ff43f8b2b3f43a7ade2ff2a3fda72b5ef";
		Token token = new Token();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("https://cdn.emnify.net/api/v1/authenticate");
		httpPost.setHeader("Content-Type", "application/json");
		JSONObject jrps = null;
		JSONObject json = new JSONObject();
		json.put("username", username);
		json.put("password", password);

		StringEntity stringEntity;

		try {

			stringEntity = new StringEntity(json.toString());
			httpPost.setEntity(stringEntity);
			HttpResponse response = httpClient.execute(httpPost);
			String rspstr = EntityUtils.toString(response.getEntity());
			jrps = new JSONObject(rspstr);
			token.setAuthToken(jrps.getString("auth_token"));
			token.setRefrToken(jrps.getString("refresh_token"));

		} catch (Exception e) {
			logger.info("  error request emnify authentication ");

		}
		return token;
	}

	/* retorna estado 201 creado SMS */
	private String requestGeepy(long geEmnifyId, String payload) {

		if (Long.valueOf(geEmnifyId) == null) {
			logger.info(" geEmnifyId is null , command "+ payload+" error communication");
			return "geEmnifyId is null ";
		}

		logger.info("request Geepy " + "  geEmnifyId " + geEmnifyId + " command " + payload);
		Token token = this.authentication();
		String source_address = "12345689";

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("https://cdn.emnify.net/api/v1/endpoint/" + geEmnifyId + "/sms");
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Authorization", "Bearer " + token.getAuthToken());

		JSONObject jrps = null;
		JSONObject json = new JSONObject();
		json.put("source_address", source_address);
		json.put("payload", payload);

		StringEntity stringEntity;
		try {
			stringEntity = new StringEntity(json.toString());
			httpPost.setEntity(stringEntity);
			HttpResponse response = httpClient.execute(httpPost);
			String rspstr = EntityUtils.toString(response.getEntity());

			return rspstr;
			// jrps = new JSONObject(rspstr);
		} catch (Exception e) {
			logger.info("  error request Geepy " + "  geEmnifyId " + geEmnifyId + " command " + payload);
		}
		
		logger.info("  error request Geepy " + "  geEmnifyId " + geEmnifyId + " command " + payload);
		return jrps.toString();

	}

	

	public String getLocation(long geEmnifyId) {
		return this.requestGeepy(geEmnifyId, "<=CMD:GPS");
	}

	public void statusGeofence(long geEmnifyId, char biGeoStatus) {
		if (biGeoStatus == 'E') {
			requestGeepy(geEmnifyId, "<=GPS:SETHOME");// fija el punto
			requestGeepy(geEmnifyId, "<=GEOFENCE:ON");
			logger.info("  <=GEOFENCE:ON  "+geEmnifyId);
			
			
		}
		if (biGeoStatus == 'D') {
			requestGeepy(geEmnifyId, "<=GEOFENCE:OFF");
			logger.info("  <=GEOFENCE:OFF  "+geEmnifyId);
			
		}
	}

	public String setRadiusGeofence(long geEmnifyId, long biGeoRadius) {
		String command = "<=GPS:SETBOUND=" + biGeoRadius;
		logger.info(command+"    " +geEmnifyId);
		
		return this.requestGeepy(geEmnifyId, command);

	}

	/* solicita puntos cada 10 segundos */
	public void requestLocationEveryTenSeconds(long geEmnifyId) {
		this.requestGeepy(geEmnifyId, "<=GPS:INT=10");

	}

	public String stopRequestLocation(long geEmnifyId) {
		return this.requestGeepy(geEmnifyId, "<=GPS:INT=300");
	}

	public void setBiIdGeepy(long geEmnifyId, long biId) {
		logger.info("set BiId geepy" +geEmnifyId + "<=ID:SETID=" + biId);
		
		this.requestGeepy(geEmnifyId, "<=ID:SETID=" + biId);

	}



}
