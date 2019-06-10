package com.hiveag.geepy.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiveag.geepy.dao.BikeDAO;
import com.hiveag.geepy.dto.BikeDTO;
import com.hiveag.geepy.dto.Body;
import com.hiveag.geepy.pojo.Bike;
import com.hiveag.geepy.pojo.Geepy;
import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.pojo.Record;
import com.hiveag.geepy.pojo.Track;

@Service
public class BikeService {

	private  static final Logger logger = LoggerFactory.getLogger(BikeService.class);

	@Autowired
	BikeDAO bikeDAO;

	@Autowired
	RecordService recordService;

	@Autowired
	TrackService trackService;

	@Autowired
	PictureService pictureService;

	@Autowired
	GeepyService geepyService;

	@Autowired
	EmnifyService emnifyService;

	@Transactional
	public Bike create(Bike bike) {
		bike.setBiStatus('O');
		bike.setBiGeoStatus('D');
		bike.setBiGeoRadius((short) 50);
		bikeDAO.create(bike);
		logger.info("  Create bike    "+bike.getBiId());

		
		return bike;
	}

	@Transactional
	public Bike findById(long biId) {
		return bikeDAO.findById(biId);
	}

	@Transactional
	public Bike findById2(long biId) {
		return bikeDAO.findById2(biId).get(0);
	}

	@Transactional
	public Bike update(Bike bike) {
		bikeDAO.update(bike);
		logger.info("  update  bike    "+bike.getBiId());
		
		return bikeDAO.findById(bike.getBiId());

	}

	@Transactional
	public Bike updateGeofence(Bike bike) {

		Record record = recordService.findByBiId(bike.getBiId());
		Bike bikeBD = record.getBike();
			if (bike.getBiGeoStatus() == 'E' || bike.getBiGeoStatus() == 'D') {
				if (bike.getBiGeoStatus() != bikeBD.getBiGeoStatus()) {
					bikeBD.setBiGeoStatus(bike.getBiGeoStatus());
					emnifyService.statusGeofence(record.getGeepy().getGeEmnifyId(), bike.getBiGeoStatus());
					logger.info("log   status  geofence  "+bike.getBiGeoStatus()+" bike   "+bike.getBiId());
				}
			}

			if (bike.getBiGeoRadius() != null) {
				bike.setBiGeoRadius(limitBiGeoRadius(bike.getBiGeoRadius()));

				if( !bike.getBiGeoRadius().equals(bikeBD.getBiGeoRadius()))
				{emnifyService.setRadiusGeofence(record.getGeepy().getGeEmnifyId(), bike.getBiGeoRadius());
			     	bikeBD.setBiGeoRadius(bike.getBiGeoRadius());
			     	logger.info("  radius  geofence  "+bike.getBiGeoRadius()+" bike   "+bike.getBiId());
			     	
				}
			}

			Track track = trackService.getLastTrackBiId(record.getBike().getBiId());
			if (track == null) {
				track=new Track();
				track.setTrLatitude(new Double(0));
				track.setTrLongitude(new Double(0));
			}

			bikeBD.setBiGeoLat(track.getTrLatitude());
			bikeBD.setBiGeoLon(track.getTrLongitude());

			bikeDAO.update(bikeBD);
		return bikeBD;

	}

	@Transactional
	public boolean lostBike(Bike bike) {
		Record record = recordService.findByBiId(bike.getBiId());
		Bike bikeBD = bikeDAO.findById(bike.getBiId());
		boolean estadoBike = false;
		try {
			if (bikeBD.getBiStatus() == 'S') {
				bikeBD.setBiStatus('O');
				emnifyService.stopRequestLocation(record.getGeepy().getGeEmnifyId());
				estadoBike = false;
				logger.info(" bike enable "+bike.getBiId());

				
			} else {
				bikeBD.setBiStatus('S');
				bikeBD.setBiGeoStatus('D');
				emnifyService.requestLocationEveryTenSeconds(record.getGeepy().getGeEmnifyId());
				estadoBike = true;
				logger.info(" bike stolen "+bike.getBiId());

			}
		} catch (Exception e) {
			logger.info("  error service lostbike    "+bike.getBiId());
		}

		bikeDAO.update(bikeBD);
		return estadoBike;
	}

	@Transactional
	public void locationBike(Record record) {
		logger.info("  location bike with geEmnifyId  "+record.getGeepy().getGeEmnifyId());
		emnifyService.getLocation(record.getGeepy().getGeEmnifyId());
	}

	@Transactional
	public Body getLastLocation(Record record) {

		Bike bike = record.getBike();
		Body geepyDTO = new Body();
		Track track;

		track = trackService.getLastTrackBiId(record.getBike().getBiId());
		if (track == null) {
			track = new Track();
			track.setTrLatitude(0);
			track.setTrLongitude(0);
			geepyDTO.setTime_gps(new Timestamp(System.currentTimeMillis()));

		}

		geepyDTO.setBiId(bike.getBiId());
		geepyDTO.setRadius(bike.getBiGeoRadius());

		geepyDTO.setLatitude(track.getTrLatitude());
		geepyDTO.setLongitude(track.getTrLongitude());

		geepyDTO.setBiGeoLat(bike.getBiGeoLat());
		geepyDTO.setBiGeoLon(bike.getBiGeoLon());

		if (bike.getBiGeoLat() == null) {
			geepyDTO.setBiGeoLat(track.getTrLatitude());
		}
		if (bike.getBiGeoLat() == null) {
			geepyDTO.setBiGeoLon(track.getTrLongitude());
		}

		geepyDTO.setBiStatus(bike.getBiGeoStatus());
		geepyDTO.setBiGeoStatus(bike.getBiGeoStatus());
		logger.info("  last location bike "+bike.getBiId());
		return geepyDTO;

	}

	@Transactional
	public boolean delete(Bike bike) {
		Record record = recordService.findByBiId(bike.getBiId());
		if (record == null)
			return false;
		Geepy geepy = geepyService.findById(record.getGeepy().getGeImei());
		geepy.setPerson(null);
		geepy.setGeState('D');
		geepyService.update(geepy);
		recordService.delete(record);
		
		return true;
	}

	@Transactional
	public boolean existBike(Bike bike) {
		Bike b = bikeDAO.findById(bike.getBiId());
		if (b == null)
			return false;
		return true;
	}

	@Transactional
	public Bike findBySerial(String biSerial) {
		Bike bike = null;
		try {
			bike = bikeDAO.findBySerial(biSerial);
		} catch (Exception e) {
			
			logger.info("  bike not found "+biSerial);
		}
		return bike;
	}

	@Transactional
	public List<Bike> getAll() {
		return bikeDAO.findAll();
	}

	@Transactional
	public List<BikeDTO> bikesPerson(Long peId) {

		List<Object[]> rows = bikeDAO.bikesPersonNative(peId);
		List<BikeDTO> bikes = new ArrayList<BikeDTO>();

		for (Object[] row : rows) {
			BikeDTO bike = new BikeDTO();

			bike.setBiId(toString(row[0]));
			bike.setBiHexColor(toString(row[1]));
			bike.setBiSerial(toString(row[2]));
			bike.setBiYear(toString(row[3]));
			bike.setBiStatus(toString(row[4]));

			bike.setCaId(toString(row[5]));
			bike.setCaName(toString(row[6]));
			bike.setCaType(toString(row[7]));

			bike.setGeImei(toString(row[9]));
			bike.setGeSerial(toString(row[10]));
			bike.setGeImsi(toString(row[11]));
			bike.setGeMsisdn(toString(row[12]));
			bike.setGeState(toString(row[13]));

			bike.setPiPath(toString(row[14]));
			bike.setBiGeoStatus(toString(row[15]));
			bike.setBiGeoRadius(toString(row[16]));

			bikes.add(bike);

		}
		return bikes;

	}

	public String toString(Object object) {
		String s = Optional.ofNullable(object).map(Object::toString).orElse("null");
		return s;

	}


	@Transactional
	public Person getPersonFromBiId(long biId) {
		return bikeDAO.getPersonFromBiId(biId);
	}

	private Short limitBiGeoRadius(Short biGeoRadius) {
		if (biGeoRadius >= 750 || biGeoRadius >= 1000)
			return 1000;
		if (biGeoRadius >= 350 && biGeoRadius < 750)
			return 500;
		if (biGeoRadius >= 100 && biGeoRadius < 350)
			return 100;
		return 50;

	}

}
