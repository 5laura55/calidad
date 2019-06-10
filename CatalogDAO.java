package com.hiveag.geepy.dao;

import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Catalog;


import java.util.List;

@Repository
public class CatalogDAO extends AbstractDAO<Catalog, Integer> {

	public CatalogDAO() {
		// TODO Auto-generated constructor stub
		super(Catalog.class);
	}

	@SuppressWarnings("unchecked")
	public List<Catalog> getBrands() {
		try {
			return  getCurrentSession()
				    .createQuery("from Catalog c where c.catalog is null")
				    .list();

		  } catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Catalog> getChild(int caId) {
		List<Catalog> modelos=null;
		try {
			modelos = getCurrentSession().createQuery("from Catalog c "
														+ " where "
														+ "  c.catalog.caId = :caId")
														.setParameter("caId", caId)
														.list();
			
		} catch (RuntimeException re) {
			log.error("getting by pePhNumber failed", re);
			throw re;
		}
		return modelos;
	}
	
	@SuppressWarnings("unchecked")
	public Catalog getParent(int caId) {
		Catalog catalog=null;
		try {
			List results =getCurrentSession().createQuery("from Catalog c "
														+ " where c.caId in ( select ca.catalog.caId "
														+ "                  from Catalog  ca"
														+ "                   where ca.caId=:caId  )  ")
														
														.setParameter("caId", caId)
														.getResultList();
			
			if(results.size()>=1) {
				
				return (Catalog)results.get(0);
			}
			
			
		} catch (RuntimeException re) {
			log.error("getting by caid failed", re);
			
		}
		return catalog;
		
	}
	
	

}