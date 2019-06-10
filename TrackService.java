package com.hiveag.geepy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hiveag.geepy.dao.TrackDAO;
import com.hiveag.geepy.pojo.Track;
import com.hiveag.geepy.pojo.TrackId;

@Service
public class TrackService {

	@Autowired
	TrackDAO trackDAO;

	@Autowired
	BikeService bikeService;

	@Transactional
	public Track create(Track track) {
		trackDAO.create(track);
		return track;
	}

	@Transactional
	public Track getById(TrackId id) {
		return trackDAO.findById(id);
	}

	@Transactional
	public Track getLastTrackBiId(long biId) {
		return trackDAO.getLastTrackBiId(biId);
	}

	@Transactional
	public List<Track> getByBikeId(long biId) {
		return trackDAO.findByBiId(biId);
	}

	@Transactional
	public List<Track> getAll() {
		return trackDAO.findAll();

	}

}
