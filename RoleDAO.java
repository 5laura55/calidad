package com.hiveag.geepy.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Role;

@Repository
public class RoleDAO extends AbstractDAO<Role, Short> {

	public RoleDAO() {
		// TODO Auto-generated constructor stub
		super(Role.class);
	}
	
	@SuppressWarnings("unchecked")
	public List < Role > findRoles(long peId){
		
		log.debug("Getting by role " + Role.class + " instance");
		
		List< Role > roles = null;
		
		try {
			
			String hql = "select r from Role r "
					+ "inner join fetch r.authorities a "
					+ "where a.id.peId = :peId";
			roles = getCurrentSession()
					.createQuery( hql)
					.setParameter("peId", peId)
					.list();

			log.debug("getting by role successful");
		} catch (RuntimeException re) {
			log.error("getting by role failed", re);
			throw re;
		}
		
		return roles;
	}

}
