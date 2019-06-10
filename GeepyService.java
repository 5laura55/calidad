package com.hiveag.geepy.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiveag.geepy.dao.GeepyDAO;
import com.hiveag.geepy.pojo.Geepy;



@Service
public class GeepyService {
	private  static final Logger logger = LoggerFactory.getLogger(GeepyService.class);
	
	@Autowired
	private GeepyDAO geepyDAO;
	
	@Transactional
	public Geepy findById(long geImei) {
		return geepyDAO.findById(geImei);
	}
	
	@Transactional
	public void create(Geepy geepy) {
		geepyDAO.create(geepy);
	}
	
	@Transactional
	public void update(Geepy geepy) {
		   geepyDAO.update(geepy);
		 
	}
	
	
	@Transactional
	public Geepy assignGeepy(Geepy geepy) {
		 Geepy geepy1 =geepyDAO.findById(geepy.getGeImei());
		 geepy1.setGeState('D');
		 geepy1.setPerson( geepy.getPerson());
		 geepyDAO.update(geepy1);
		 
		 logger.info("  Assign geepy  " + geepy.getGeImei() + "to person " + geepy1.getPerson().getPeId() );
		 return geepy1;
		 
	}
	
	
	/*retorna true si el geepy no se encuentra asignado a una persona
	 * precondicion el geepy debe exitir en la base de datos
	 * */
	@Transactional
	public boolean geepyAsignado(Geepy geepy)
	{   Geepy geepy1= geepyDAO.findById(geepy.getGeImei());
		if(geepy1.getPerson()==null) {
			return false;
		}
		return true;
	}
	
	
	
	@Transactional
	public List<Geepy> getAll() {
		List<Geepy>  list=geepyDAO.getAll();
		return list;
	}
	

}
