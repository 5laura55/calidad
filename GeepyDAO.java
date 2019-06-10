package com.hiveag.geepy.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Geepy;

@Repository
public class GeepyDAO extends AbstractDAO<Geepy, Long> {

	public GeepyDAO() {
		// TODO Auto-generated constructor stub
		super(Geepy.class);
	}

	@SuppressWarnings("unchecked")
	public List<Geepy> getAll() {
		List<Geepy> listGeepy = null;
		try {
			listGeepy = getCurrentSession().createQuery("from Geepy").list();

		} catch (RuntimeException re) {
			throw re;
		}

		return listGeepy;
	}
	
	/***geepy que no han sido asignados a alguna bicicleta*/
	@SuppressWarnings("unchecked")
	public List<Geepy> geepyPerson(Long peId) {
		List<Geepy> listGeepy = null;
		
		try {
			listGeepy = getCurrentSession()
					.createQuery("from Geepy g "
							+ "  inner join g.records where g.peId=:peId ")
					.setParameter("peId", peId)
					.list();

		} catch (RuntimeException re) {
			throw re;
		}

		return listGeepy;
	}
}
