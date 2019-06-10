package com.hiveag.geepy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiveag.geepy.dao.LocationDAO;
import com.hiveag.geepy.pojo.Location;

@Service
public class LocationService {
	
	@Autowired
	LocationDAO locationDAO;
	
	@Transactional
	public Location getById( int loId ){
				
		return locationDAO.findById( loId );
		
	}
	
	@Transactional
	public List < Location > getAll(){
			
		return locationDAO.findAllLoc();
		
	}

}
