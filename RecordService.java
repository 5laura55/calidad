package com.hiveag.geepy.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiveag.geepy.dao.RecordDAO;
import com.hiveag.geepy.pojo.Bike;
import com.hiveag.geepy.pojo.Geepy;
import com.hiveag.geepy.pojo.Record;
import com.hiveag.geepy.pojo.RecordId;

@Service
public class RecordService {
	private static final Logger logger = LoggerFactory.getLogger(RecordService.class);

	@Autowired
	RecordDAO recordDAO;

	@Autowired
	BikeService bikeService;

	@Autowired
	PersonService personService;

	@Autowired
	GeepyService geepyService;

	@Autowired
	EmnifyService emnifyService;

	@Transactional
	public Record create(Record record) {

		Geepy geepyBD = geepyService.assignGeepy(record.getGeepy());
		Bike bike = bikeService.create(record.getBike());
		RecordId recordId = new RecordId();
		recordId.setBiId(bike.getBiId());
		recordId.setGeImei(record.getGeepy().getGeImei());
		record.setId(recordId);
		record.setReState(true);
		record.setReTimestamp(LocalDateTime.now());
		recordDAO.create(record);

		emnifyService.setBiIdGeepy(geepyBD.getGeEmnifyId(), bike.getBiId());
		logger.info("create new bike  " + record.getBike().getBiId());

		return record;
	}

	@Transactional
	public Record findByBiId(Long biId) {
		return recordDAO.findByBiId(biId);

	}

	@Transactional
	public List<Record> findAll() {
		return recordDAO.findAll();

	}

	@Transactional
	public void delete(Record record) {
		recordDAO.delete(record);

	}

}
