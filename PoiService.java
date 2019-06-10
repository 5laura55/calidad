package com.hiveag.geepy.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hiveag.geepy.dao.PoiDAO;
import com.hiveag.geepy.dto.PoiLimitDTO;
import com.hiveag.geepy.pojo.Poi;

@Service
public class PoiService {
	private  static final Logger logger = LoggerFactory.getLogger(PoiService.class);

	@Autowired
	PoiDAO poiDAO;

	@Transactional
	public Poi create(Poi poi) {
		poiDAO.create(poi);
		return poi;
	}

	@Transactional
	public boolean delete(Poi poi) {
		poi = poiDAO.findById(poi.getPoId());
		if (poi == null)
			return false;
		poiDAO.delete(poi);
		return true;
	}

	@Transactional
	public Poi get(long poId) {
		return poiDAO.findById(poId);

	}

	@Transactional
	public List<Poi> getPoints(PoiLimitDTO limit) {
		List<Object[]> rows = poiDAO.pointsRadio(limit);
		logger.info("points limit distance, lat, lon " + limit.getDistance()+"  "+limit.getLat()+"  "+limit.getLng());
		return mapperList(rows);
		

	}
	@Transactional
	public List<Poi>  mapperList(List<Object[]> rows){
		List<Poi> listPoi=new ArrayList<Poi>();
		Poi poi=new Poi();
		long poId;
		for (Object[] row : rows) { 
			
			poId=Long.valueOf(row[0].toString());
			poi=poiDAO.findById(poId);
			  listPoi.add(poi);
			
		}
		return listPoi;
	}
	

	@Transactional
	public void update(Poi poi) {
		poiDAO.update(poi);
	}

}
