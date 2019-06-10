package com.hiveag.geepy.dao;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Bike;
import com.hiveag.geepy.pojo.Person;





@Repository
public class BikeDAO extends AbstractDAO<Bike, Long> {

	private  static final Logger logger = LoggerFactory.getLogger(BikeDAO.class);


	public BikeDAO() {
		// TODO Auto-generated constructor stub
		super(Bike.class);
	}
	
	
	public Bike findBySerial(String biSerial) {
		Bike bike= null;
		try {
			bike= (Bike) getCurrentSession()
					.createQuery("from Bike b where b.biSerial= :biSerial")
					.setParameter("biSerial", biSerial).getSingleResult();
		} catch (RuntimeException re) {
			throw re;
		}

		return bike;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Bike> findById2(Long biId) {
		List<Bike> list= null;
		
		try {
			list= getCurrentSession()
					.createQuery(" from Bike b "
							+ "    inner join  b.catalog c"
							+ "    inner join   b.records r "
							+ "    inner join r.geepy g "
				 			+ "   where b.biId=:biId ")
					  .setParameter("biId", biId)
					.getResultList();

		} catch (RuntimeException re) {
			logger.info("error list catalog bike");
			throw re;
		}
		return list;
	}
	
	
	
	public List<Object[]>  bikesPersonNative(Long peId) {
		List<Object[]> rows = null;
		
		try {
			rows= getCurrentSession()
					.createNativeQuery
					(" select  b.bi_id,  b.bi_hex_color,   b.bi_serial,    b.bi_year,   b.bi_status  , c.ca_id, c.ca_name, c.ca_type, c.ca_parent_id,  g.ge_imei,  g.ge_serial,  g.ge_imsi,  g.ge_msisdn,  g.ge_state     ,(select pi_path from picture where  picture.bi_id=b.bi_id limit 1) as pi_path, b.bi_geo_status, b.bi_geo_radius   " + 
							"							  from bike b   " + 
							"							  inner join catalog c on c.ca_id=b.ca_id   " + 
							"							  inner join record r on b.bi_id=r.bi_id  " + 
							"							 inner join geepy g  on g.ge_imei=r.ge_imei  " + 
							"							 where g.pe_id=:peId  ")
					  .setParameter("peId", peId)
					.list();

		} catch (RuntimeException re) {
			throw re;
		}
		
		logger.info(" list bikes from " + peId);
		return rows;
	}
	
	
	
	public Person getPersonFromBiId(long biId) {
		Person person = null;
		
		try {
			person =(Person) getCurrentSession()
					.createQuery(" Select p From Record  r"
							+ "    inner join  r.geepy g"
							+ "    inner join  g.person p "
							+ "   where r.bike.biId=:biId ")
					  .setParameter("biId", biId)
					.getSingleResult();

		} catch (RuntimeException re) {
			throw re;
		}
		return person ;
	}
	
	
	
	
	
	
	
}
