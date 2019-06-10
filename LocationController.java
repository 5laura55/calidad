package com.hiveag.geepy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.pojo.Location;
import com.hiveag.geepy.service.LocationService;
import com.hiveag.geepy.util.View;

@Controller
public class LocationController {
	
	@Autowired
	LocationService locationService;
	
	@RequestMapping( value = "/api/location", method = RequestMethod.GET )
	@JsonView(View.Summary.class)
	@ResponseBody
	public List< Location > getAll(){
		
		return locationService.getAll();
		
	}
	
	@RequestMapping( value = "/api/location/{id}", method = RequestMethod.GET )
	@JsonView(View.Summary.class)
	@ResponseBody
	public Location get( @PathVariable( "id" ) int loId ){
		
		return locationService.getById(loId);
		
	}

}
