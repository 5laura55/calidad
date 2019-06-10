package com.hiveag.geepy.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Location;

@Repository
public class LocationDAO extends AbstractDAO<Location,Integer>{

	public LocationDAO() {
		// TODO Auto-generated constructor stub
		super(Location.class);
	}

	
	@SuppressWarnings("unchecked")
	public List<Location> findAllLoc(){

		log.debug("Getting All " + Location.class + " instance");
		
		List<Location> locations = null;
		
		try {
			locations = getCurrentSession()
					.createQuery( "from Location l where l.loType = 'C' order by l.loAlpha" )
					.list();
			log.debug("getting all successful");
		} catch (RuntimeException re) {
			log.error("getting all failed", re);
			throw re;
		}
		
		return locations;
	}
}
