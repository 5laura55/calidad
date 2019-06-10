
package com.hiveag.geepy.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.pojo.Track;
import com.hiveag.geepy.pojo.TrackId;

@Repository
public class TrackDAO extends AbstractDAO<Track, TrackId> {

	public TrackDAO() {
		// TODO Auto-generated constructor stub
		super(Track.class);
	}

	@SuppressWarnings("unchecked")
	public List<Track> findByBiId(long biId) {
		List<Track> tracks = null;
		try {
			tracks = getCurrentSession().createQuery("from Track t where t.id.biId= :biId").setParameter("biId", biId)
					.list();
			return tracks;

		} catch (RuntimeException re) {
			log.error("getting by biId failed", re);
			throw re;
		}

	}
	
	
	@SuppressWarnings("unchecked")
	public Track getLastTrackBiId(long biId) {
		List<Track> tracks = null;
		try {
			tracks = getCurrentSession().createQuery("from Track t where t.id.biId= :biId order by t.id.trTimestamp desc").setParameter("biId", biId)
					.list();
			if(tracks!=null)
			return tracks.get(0);
		    

		} catch (RuntimeException re) {
			return null;
		}
		return null;

	}
}
